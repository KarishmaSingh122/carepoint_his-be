package com.suma.carepoint.services.medication;

import com.suma.carepoint.entities.medication.Medication;
import com.suma.carepoint.entities.medication.Prescription;
import com.suma.carepoint.entities.medication.PrescriptionItem;
import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.medication.*;
import com.suma.carepoint.repositories.medication.MedicationRepository;
import com.suma.carepoint.repositories.medication.PrescriptionItemRepository;
import com.suma.carepoint.repositories.medication.PrescriptionRepository;
import com.suma.carepoint.repositories.patient.PatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class MedicationServiceImpl implements MedicationService {

    private final ModelMapper modelMapper;
    private final MedicationRepository medicationRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionItemRepository prescriptionItemRepository;
    private final PatientRepository patientRepository;

    public MedicationServiceImpl(
            ModelMapper modelMapper,
            MedicationRepository medicationRepository,
            PrescriptionRepository prescriptionRepository,
            PrescriptionItemRepository prescriptionItemRepository,
            PatientRepository patientRepository
    ) {
        this.modelMapper = modelMapper;
        this.medicationRepository = medicationRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.prescriptionItemRepository = prescriptionItemRepository;
        this.patientRepository = patientRepository;
    }

    
    @Override
    public ApiResponse createMedication(CreateMedicationRequest request) {

        try {

            if (request == null) {
                return new ApiResponse(
                        2,
                        "Medication request cannot be null",
                        null,
                        0L
                );
            }

            String medicationName = normalize(request.getName());

            if (medicationName == null) {
                return new ApiResponse(
                        2,
                        "Medication name is required",
                        null,
                        0L
                );
            }

            
            if (medicationRepository.existsByNameIgnoreCase(medicationName)) {

                return new ApiResponse(
                        2,
                        "Medication with name '" + medicationName
                                + "' already exists",
                        null,
                        0L
                );
            }

            
            Medication medication =
                    modelMapper.map(request, Medication.class);

            
            medication.setName(medicationName);

            medication.setGenericName(
                    normalize(request.getGenericName())
            );

            medication.setStrength(
                    normalize(request.getStrength())
            );

            
            medication.setActive(true);

            Medication savedMedication =
                    medicationRepository.save(medication);

            MedicationResponse response =
                    modelMapper.map(
                            savedMedication,
                            MedicationResponse.class
                    );

            return new ApiResponse(
                    1,
                    "Medication created successfully",
                    response,
                    1L
            );

        } catch (DataIntegrityViolationException e) {

            log.error(
                    "Data integrity error while creating medication: {}",
                    request != null ? request.getName() : null,
                    e
            );

            return new ApiResponse(
                    2,
                    "Medication could not be created because the data already exists or violates a database constraint",
                    null,
                    0L
            );

        } catch (Exception e) {

            log.error(
                    "Unexpected error while creating medication",
                    e
            );

            return new ApiResponse(
                    2,
                    "Failed to create medication",
                    null,
                    0L
            );
        }
    }

    
    @Override
    @Transactional(readOnly = true)
    public ApiResponse getMedications() {

        try {

            List<Medication> medications =
                    medicationRepository.findAll();

            List<MedicationResponse> responses =
                    medications.stream()
                            .map(medication ->
                                    modelMapper.map(
                                            medication,
                                            MedicationResponse.class
                                    )
                            )
                            .toList();

            return new ApiResponse(
                    1,
                    "Medications fetched successfully",
                    responses,
                    Long.valueOf(responses.size())
            );

        } catch (Exception e) {

            log.error(
                    "Error while fetching medications",
                    e
            );

            return new ApiResponse(
                    2,
                    "Failed to fetch medications",
                    null,
                    0L
            );
        }
    }

    
    @Override
    @Transactional(readOnly = true)
    public ApiResponse getMedicationById(Long medicationId) {

        try {

            if (medicationId == null || medicationId <= 0) {

                return new ApiResponse(
                        2,
                        "Valid medication ID is required",
                        null,
                        0L
                );
            }

            Medication medication =
                    medicationRepository.findById(medicationId)
                            .orElse(null);

            if (medication == null) {

                return new ApiResponse(
                        2,
                        "Medication not found with ID: "
                                + medicationId,
                        null,
                        0L
                );
            }

            MedicationResponse response =
                    modelMapper.map(
                            medication,
                            MedicationResponse.class
                    );

            return new ApiResponse(
                    1,
                    "Medication fetched successfully",
                    response,
                    1L
            );

        } catch (Exception e) {

            log.error(
                    "Error while fetching medication with ID: {}",
                    medicationId,
                    e
            );

            return new ApiResponse(
                    2,
                    "Failed to fetch medication",
                    null,
                    0L
            );
        }
    }

    
    @Override
    public ApiResponse updateMedication(
            Long medicationId,
            UpdateMedicationRequest request
    ) {

        try {

            if (medicationId == null || medicationId <= 0) {

                return new ApiResponse(
                        2,
                        "Valid medication ID is required",
                        null,
                        0L
                );
            }

            if (request == null) {

                return new ApiResponse(
                        2,
                        "Medication request cannot be null",
                        null,
                        0L
                );
            }

            Medication medication =
                    medicationRepository.findById(medicationId)
                            .orElse(null);

            if (medication == null) {

                return new ApiResponse(
                        2,
                        "Medication not found with ID: "
                                + medicationId,
                        null,
                        0L
                );
            }

            String medicationName =
                    normalize(request.getName());

            if (medicationName == null) {

                return new ApiResponse(
                        2,
                        "Medication name is required",
                        null,
                        0L
                );
            }

            
            boolean duplicateName =
                    medicationRepository
                            .existsByNameIgnoreCaseAndMedicationIdNot(
                                    medicationName,
                                    medicationId
                            );

            if (duplicateName) {

                return new ApiResponse(
                        2,
                        "Another medication with name '"
                                + medicationName
                                + "' already exists",
                        null,
                        0L
                );
            }

            
            medication.setName(medicationName);

            medication.setGenericName(
                    normalize(request.getGenericName())
            );

            medication.setStrength(
                    normalize(request.getStrength())
            );

            Medication updatedMedication =
                    medicationRepository.save(medication);

            MedicationResponse response =
                    modelMapper.map(
                            updatedMedication,
                            MedicationResponse.class
                    );

            return new ApiResponse(
                    1,
                    "Medication updated successfully",
                    response,
                    1L
            );

        } catch (DataIntegrityViolationException e) {

            log.error(
                    "Data integrity error while updating medication ID: {}",
                    medicationId,
                    e
            );

            return new ApiResponse(
                    2,
                    "Medication could not be updated because the data violates a database constraint",
                    null,
                    0L
            );

        } catch (Exception e) {

            log.error(
                    "Error while updating medication ID: {}",
                    medicationId,
                    e
            );

            return new ApiResponse(
                    2,
                    "Failed to update medication",
                    null,
                    0L
            );
        }
    }

    
    @Override
    public ApiResponse updateMedicationStatus(
            Long medicationId,
            MedicationStatusRequest request
    ) {

        try {

            if (medicationId == null || medicationId <= 0) {

                return new ApiResponse(
                        2,
                        "Valid medication ID is required",
                        null,
                        0L
                );
            }

            if (request == null || request.getActive() == null) {

                return new ApiResponse(
                        2,
                        "Active status is required",
                        null);
            }

            Medication medication =
                    medicationRepository.findById(medicationId)
                            .orElse(null);

            if (medication == null) {

                return new ApiResponse(
                        2,
                        "Medication not found with ID: "
                                + medicationId,
                        null,
                        0L
                );
            }

            medication.setActive(request.getActive());

            Medication updatedMedication =
                    medicationRepository.save(medication);

            MedicationResponse response =
                    modelMapper.map(
                            updatedMedication,
                            MedicationResponse.class
                    );

            String message =
                    Boolean.TRUE.equals(request.getActive())
                            ? "Medication activated successfully"
                            : "Medication deactivated successfully";

            return new ApiResponse(
                    1,
                    message,
                    response,
                    1L
            );

        } catch (Exception e) {

            log.error(
                    "Error while updating medication status. ID: {}",
                    medicationId,
                    e
            );

            return new ApiResponse(
                    2,
                    "Failed to update medication status",
                    null,
                    0L
            );
        }
    }

    
    @Override
    public ApiResponse deleteMedication(Long medicationId) {

        try {

            if (medicationId == null || medicationId <= 0) {

                return new ApiResponse(
                        2,
                        "Valid medication ID is required",
                        null,
                        0L
                );
            }

            Medication medication =
                    medicationRepository.findById(medicationId)
                            .orElse(null);

            if (medication == null) {

                return new ApiResponse(
                        2,
                        "Medication not found with ID: "
                                + medicationId,
                        null,
                        0L
                );
            }

            
            boolean usedInPrescription =
                    prescriptionItemRepository
                            .existsByMedicationMedicationId(
                                    medicationId
                            );

            if (usedInPrescription) {

                return new ApiResponse(
                        2,
                        "Medication cannot be deleted because it is already used in a prescription. Deactivate it instead.",
                        null,
                        0L
                );
            }

            medicationRepository.delete(medication);

            return new ApiResponse(
                    1,
                    "Medication deleted successfully",
                    null,
                    0L
            );

        } catch (DataIntegrityViolationException e) {

            log.error(
                    "Data integrity error while deleting medication ID: {}",
                    medicationId,
                    e
            );

            return new ApiResponse(
                    2,
                    "Medication cannot be deleted because it is referenced by other records",
                    null,
                    0L
            );

        } catch (Exception e) {

            log.error(
                    "Error while deleting medication ID: {}",
                    medicationId,
                    e
            );

            return new ApiResponse(
                    2,
                    "Failed to delete medication",
                    null,
                    0L
            );
        }
    }

    
    @Override
    @Transactional(readOnly = true)
    public ApiResponse searchMedications(String keyword) {

        try {

            String searchKeyword = normalize(keyword);

            if (searchKeyword == null) {

                return new ApiResponse(
                        2,
                        "Search keyword is required",
                        null,
                        0L
                );
            }

            
            List<Medication> medications =
                    medicationRepository
                            .findByNameContainingIgnoreCaseAndActiveTrue(
                                    searchKeyword
                            );

            
            if (medications.isEmpty()) {

                medications =
                        medicationRepository
                                .findByGenericNameContainingIgnoreCaseAndActiveTrue(
                                        searchKeyword
                                );
            }

            List<MedicationResponse> responses =
                    medications.stream()
                            .map(medication ->
                                    modelMapper.map(
                                            medication,
                                            MedicationResponse.class
                                    )
                            )
                            .toList();

            return new ApiResponse(
                    1,
                    "Medication search completed successfully",
                    responses,
                    (long) responses.size()
            );

        } catch (Exception e) {

            log.error(
                    "Error while searching medications. Keyword: {}",
                    keyword,
                    e
            );

            return new ApiResponse(
                    2,
                    "Failed to search medications",
                    null,
                    0L
            );
        }
    }




    
    
    private String normalize(String value) {

        if (value == null) {
            return null;
        }

        String trimmedValue = value.trim();

        return trimmedValue.isEmpty()
                ? null
                : trimmedValue;
    }



    /**
     * Create prescription with medicines.
     */
    @Override
    public ApiResponse createPrescription(PrescriptionRequest request) {

        try {

            if (request == null) {
                return new ApiResponse(
                        2,
                        "Prescription request cannot be null",
                        null,
                        0L
                );
            }

            if (request.getPatientId() == null) {
                return new ApiResponse(
                        2,
                        "Patient ID is required",
                        null,
                        0L
                );
            }

            if (request.getDoctorId() == null) {
                return new ApiResponse(
                        2,
                        "Doctor ID is required",
                        null,
                        0L
                );
            }

            if (request.getItems() == null || request.getItems().isEmpty()) {
                return new ApiResponse(
                        2,
                        "At least one medicine is required",
                        null,
                        0L
                );
            }

            /*
             * Validate patient.
             */
            var patient = patientRepository.findById(request.getPatientId())
                    .orElse(null);

            if (patient == null) {
                return new ApiResponse(
                        2,
                        "Patient not found with ID: " + request.getPatientId(),
                        null,
                        0L
                );
            }

            /*
             * Validate doctor.
             */
//            var doctor = staffRepository.findById(request.getDoctorId())
//                    .orElse(null);
//
//            if (doctor == null) {
//                return new ApiResponse(
//                        2,
//                        "Doctor/Staff not found with ID: " + request.getDoctorId(),
//                        null,
//                        0L
//                );
//            }

            /*
             * Validate visit if supplied.
             */
//            var visit = request.getVisitId() != null
//                    ? visitRepository.findById(request.getVisitId()).orElse(null)
//                    : null;

//            if (request.getVisitId() != null && visit == null) {
//                return new ApiResponse(
//                        2,
//                        "Visit not found with ID: " + request.getVisitId(),
//                        null,
//                        0L
//                );
//            }

            /*
             * Create prescription.
             */
            Prescription prescription = new Prescription();

            prescription.setPatient(patient);
//            prescription.setDoctor(doctor);
//            prescription.setVisit(visit);

            prescription.setPrescriptionDate(
                    request.getPrescriptionDate()
            );

            prescription.setNotes(request.getNotes());
            prescription.setActive(true);

            /*
             * Create prescription items.
             */
            List<PrescriptionItem> items = new ArrayList<>();

            for (PrescriptionItemRequest itemRequest : request.getItems()) {

                if (itemRequest == null || itemRequest.getMedicationId() == null) {
                    return new ApiResponse(
                            2,
                            "Medication ID is required for every prescription item",
                            null,
                            0L
                    );
                }

                Medication medication = medicationRepository
                        .findById(itemRequest.getMedicationId())
                        .orElse(null);

                if (medication == null) {
                    return new ApiResponse(
                            2,
                            "Medication not found with ID: "
                                    + itemRequest.getMedicationId(),
                            null,
                            0L
                    );
                }

                if (!Boolean.TRUE.equals(medication.getActive())) {
                    return new ApiResponse(
                            2,
                            "Medication is inactive: "
                                    + medication.getName(),
                            null,
                            0L
                    );
                }

                PrescriptionItem item = new PrescriptionItem();

                item.setPrescription(prescription);
                item.setMedication(medication);
                item.setDosage(itemRequest.getDosage());
                item.setFrequency(itemRequest.getFrequency());
                item.setDuration(itemRequest.getDuration());
                item.setInstructions(itemRequest.getInstructions());

                items.add(item);
            }

            prescription.setItems(items);

            /*
             * Save prescription.
             *
             * CascadeType.ALL on Prescription.items will save
             * prescription items automatically.
             */
            Prescription savedPrescription =
                    prescriptionRepository.save(prescription);

//            PrescriptionResponse response =
//                    modelMapper.map(savedPrescription, PrescriptionResponse.class);

            PrescriptionResponse response =
                    buildPrescriptionResponse(savedPrescription);

            return new ApiResponse(
                    1,
                    "Prescription created successfully",
                    response,
                    1L
            );

        } catch (DataIntegrityViolationException ex) {

            log.error("Database constraint error while creating prescription", ex);

            return new ApiResponse(
                    2,
                    "Unable to create prescription due to a database constraint",
                    null
            );

        } catch (Exception ex) {

            log.error("Error while creating prescription", ex);

            return new ApiResponse(
                    2,
                    "Failed to create prescription",
                    null,
                    0L
            );
        }
    }

    /**
     * Get complete prescription.
     */
    @Override
    @Transactional(readOnly = true)
    public ApiResponse getPrescriptionById(Long prescriptionId) {

        try {

            if (prescriptionId == null || prescriptionId <= 0) {
                return new ApiResponse(
                        2,
                        "Valid prescription ID is required",
                        null,
                        0L
                );
            }

            Prescription prescription = prescriptionRepository
                    .findById(prescriptionId)
                    .orElse(null);

            if (prescription == null) {
                return new ApiResponse(
                        2,
                        "Prescription not found with ID: " + prescriptionId,
                        null,
                        0L
                );
            }

//            PrescriptionResponse response = modelMapper.map(prescription, PrescriptionResponse.class);
            PrescriptionResponse response = buildPrescriptionResponse(prescription);
            return new ApiResponse(
                    1,
                    "Prescription fetched successfully",
                    response,
                    1L
            );

        } catch (Exception ex) {

            log.error(
                    "Error while fetching prescription with ID: {}",
                    prescriptionId,
                    ex
            );

            return new ApiResponse(
                    2,
                    "Failed to fetch prescription",
                    null
            );
        }
    }

    /**
     * Update prescription and its medicines.
     */
    @Override
    public ApiResponse updatePrescription(
            Long prescriptionId,
            PrescriptionRequest request
    ) {

        try {

            if (prescriptionId == null || prescriptionId <= 0) {
                return new ApiResponse(
                        2,
                        "Valid prescription ID is required",
                        null,
                        0L
                );
            }

            if (request == null) {
                return new ApiResponse(
                        2,
                        "Prescription request cannot be null",
                        null,
                        0L
                );
            }

            if (request.getItems() == null || request.getItems().isEmpty()) {
                return new ApiResponse(
                        2,
                        "At least one medicine is required",
                        null,
                        0L
                );
            }

            Prescription prescription = prescriptionRepository
                    .findById(prescriptionId)
                    .orElse(null);

            if (prescription == null) {
                return new ApiResponse(
                        2,
                        "Prescription not found with ID: " + prescriptionId,
                        null,
                        0L
                );
            }

            /*
             * Update patient.
             */
            if (request.getPatientId() != null) {

                var patient = patientRepository
                        .findById(request.getPatientId())
                        .orElse(null);

                if (patient == null) {
                    return new ApiResponse(
                            2,
                            "Patient not found with ID: "
                                    + request.getPatientId(),
                            null,
                            0L
                    );
                }

                prescription.setPatient(patient);
            }

            /*
             * Update doctor.
             */
//            if (request.getDoctorId() != null) {
//
//                var doctor = staffRepository
//                        .findById(request.getDoctorId())
//                        .orElse(null);
//
//                if (doctor == null) {
//                    return new ApiResponse(
//                            2,
//                            "Doctor/Staff not found with ID: "
//                                    + request.getDoctorId(),
//                            null,
//                            0L
//                    );
//                }
//
//                prescription.setDoctor(doctor);
//            }

            /*
             * Update visit.
             */
//            if (request.getVisitId() != null) {
//
//                var visit = visitRepository
//                        .findById(request.getVisitId())
//                        .orElse(null);
//
//                if (visit == null) {
//                    return new ApiResponse(
//                            2,
//                            "Visit not found with ID: "
//                                    + request.getVisitId(),
//                            null,
//                            0L
//                    );
//                }
//
//                prescription.setVisit(visit);
//            } else {
//                prescription.setVisit(null);
//            }

            prescription.setPrescriptionDate(
                    request.getPrescriptionDate()
            );

            prescription.setNotes(request.getNotes());

            /*
             * Remove old prescription items.
             *
             * orphanRemoval=true will delete them from database.
             */
            prescription.getItems().clear();

            /*
             * Add updated prescription items.
             */
            for (PrescriptionItemRequest itemRequest : request.getItems()) {

                if (itemRequest == null ||
                        itemRequest.getMedicationId() == null) {

                    return new ApiResponse(
                            2,
                            "Medication ID is required for every prescription item",
                            null,
                            0L
                    );
                }

                Medication medication = medicationRepository
                        .findById(itemRequest.getMedicationId())
                        .orElse(null);

                if (medication == null) {
                    return new ApiResponse(
                            2,
                            "Medication not found with ID: "
                                    + itemRequest.getMedicationId(),
                            null,
                            0L
                    );
                }

                if (!Boolean.TRUE.equals(medication.getActive())) {
                    return new ApiResponse(
                            2,
                            "Medication is inactive: "
                                    + medication.getName(),
                            null,
                            0L
                    );
                }

                PrescriptionItem item = new PrescriptionItem();

                item.setPrescription(prescription);
                item.setMedication(medication);
                item.setDosage(itemRequest.getDosage());
                item.setFrequency(itemRequest.getFrequency());
                item.setDuration(itemRequest.getDuration());
                item.setInstructions(itemRequest.getInstructions());

                prescription.getItems().add(item);
            }

            Prescription updatedPrescription =
                    prescriptionRepository.save(prescription);

//            PrescriptionResponse response =
//                    modelMapper.map(
//                            updatedPrescription,
//                            PrescriptionResponse.class
//                    );

            PrescriptionResponse response = buildPrescriptionResponse(updatedPrescription);

            return new ApiResponse(
                    1,
                    "Prescription updated successfully",
                    response,
                    1L
            );

        } catch (DataIntegrityViolationException ex) {

            log.error(
                    "Database constraint error while updating prescription ID: {}",
                    prescriptionId,
                    ex
            );

            return new ApiResponse(
                    2,
                    "Unable to update prescription due to a database constraint",
                    null
            );

        } catch (Exception ex) {

            log.error(
                    "Error while updating prescription ID: {}",
                    prescriptionId,
                    ex
            );

            return new ApiResponse(
                    2,
                    "Failed to update prescription",
                    null
            );
        }
    }

    /**
     * Activate / deactivate prescription.
     */
    @Override
    public ApiResponse updatePrescriptionStatus(
            Long prescriptionId,
            Boolean active
    ) {

        try {

            if (prescriptionId == null || prescriptionId <= 0) {
                return new ApiResponse(
                        2,
                        "Valid prescription ID is required",
                        null,
                        0L
                );
            }

            if (active == null) {
                return new ApiResponse(
                        2,
                        "Active status is required",
                        null,
                        0L
                );
            }

            Prescription prescription = prescriptionRepository
                    .findById(prescriptionId)
                    .orElse(null);

            if (prescription == null) {
                return new ApiResponse(
                        2,
                        "Prescription not found with ID: " + prescriptionId,
                        null,
                        0L
                );
            }

            prescription.setActive(active);

            Prescription updatedPrescription =
                    prescriptionRepository.save(prescription);

            PrescriptionResponse response =
                    modelMapper.map(
                            updatedPrescription,
                            PrescriptionResponse.class
                    );

            String message = active
                    ? "Prescription activated successfully"
                    : "Prescription deactivated successfully";

            return new ApiResponse(
                    1,
                    message,
                    response,
                    1L
            );

        } catch (Exception ex) {

            log.error(
                    "Error while updating prescription status. ID: {}",
                    prescriptionId,
                    ex
            );

            return new ApiResponse(
                    2,
                    "Failed to update prescription status",
                    null,
                    0L
            );
        }
    }

    /**
     * Delete prescription if allowed.
     */
    @Override
    public ApiResponse deletePrescription(Long prescriptionId) {

        try {

            if (prescriptionId == null || prescriptionId <= 0) {
                return new ApiResponse(
                        2,
                        "Valid prescription ID is required",
                        null,
                        0L
                );
            }

            Prescription prescription = prescriptionRepository
                    .findById(prescriptionId)
                    .orElse(null);

            if (prescription == null) {
                return new ApiResponse(
                        2,
                        "Prescription not found with ID: " + prescriptionId,
                        null,
                        0L
                );
            }

            /*
             * Since prescription items use ON DELETE CASCADE,
             * deleting the prescription will delete its items.
             */
            prescriptionRepository.delete(prescription);

            return new ApiResponse(
                    1,
                    "Prescription deleted successfully",
                    null,
                    1L
            );

        } catch (DataIntegrityViolationException ex) {

            log.error(
                    "Database constraint error while deleting prescription ID: {}",
                    prescriptionId,
                    ex
            );

            return new ApiResponse(
                    2,
                    "Prescription cannot be deleted because it is referenced by other records",
                    null
            );

        } catch (Exception ex) {

            log.error(
                    "Error while deleting prescription ID: {}",
                    prescriptionId,
                    ex
            );

            return new ApiResponse(
                    2,
                    "Failed to delete prescription",
                    null
            );
        }
    }

    /**
     * Get patient's prescription history.
     */
    @Override
    @Transactional(readOnly = true)
    public ApiResponse getPatientPrescriptions(Long patientId) {

        try {

            if (patientId == null || patientId <= 0) {
                return new ApiResponse(
                        2,
                        "Valid patient ID is required",
                        null,
                        0L
                );
            }

            List<Prescription> prescriptions =
                    prescriptionRepository.findByPatientPatientIdOrderByPrescriptionDateDesc(
                            patientId
                    );

            List<PrescriptionResponse> response =
                    prescriptions.stream()
                            .map(prescription ->
                                    modelMapper.map(
                                            prescription,
                                            PrescriptionResponse.class
                                    )
                            )
                            .collect(Collectors.toList());

            return new ApiResponse(
                    1,
                    "Patient prescriptions fetched successfully",
                    response,
                    Long.valueOf(response.size())
            );

        } catch (Exception ex) {

            log.error(
                    "Error while fetching prescriptions for patient ID: {}",
                    patientId,
                    ex
            );

            return new ApiResponse(
                    2,
                    "Failed to fetch patient prescriptions",
                    null
            );
        }
    }

    @Override
    public ApiResponse getVisitPrescriptions(Long visitId) {
        return null;
    }

    @Override
    public ApiResponse getDoctorPrescriptions(Long doctorId) {
        return null;
    }

    /**
     * Get prescriptions for a visit.
     */
//    @Override
//    @Transactional(readOnly = true)
//    public ApiResponse getVisitPrescriptions(Long visitId) {
//
//        try {
//
//            if (visitId == null || visitId <= 0) {
//                return new ApiResponse(
//                        2,
//                        "Valid visit ID is required",
//                        null,
//                        0L
//                );
//            }
//
//            List<Prescription> prescriptions =
//                    prescriptionRepository.findByVisitVisitIdOrderByPrescriptionDateDesc(
//                            visitId
//                    );
//
//            List<PrescriptionResponse> response =
//                    prescriptions.stream()
//                            .map(prescription ->
//                                    modelMapper.map(
//                                            prescription,
//                                            PrescriptionResponse.class
//                                    )
//                            )
//                            .collect(Collectors.toList());
//
//            return new ApiResponse(
//                    1,
//                    "Visit prescriptions fetched successfully",
//                    response,
//                    Long.valueOf(response.size())
//            );
//
//        } catch (Exception ex) {
//
//            log.error(
//                    "Error while fetching prescriptions for visit ID: {}",
//                    visitId,
//                    ex
//            );
//
//            return new ApiResponse(
//                    2,
//                    "Failed to fetch visit prescriptions",
//                    null
//            );
//        }
//    }

    /**
     * Get prescriptions created by doctor.
     */
//    @Override
//    @Transactional(readOnly = true)
//    public ApiResponse getDoctorPrescriptions(Long doctorId) {
//
//        try {
//
//            if (doctorId == null || doctorId <= 0) {
//                return new ApiResponse(
//                        2,
//                        "Valid doctor ID is required",
//                        null,
//                        0L
//                );
//            }
//
//            List<Prescription> prescriptions =
//                    prescriptionRepository.findByDoctorStaffIdOrderByPrescriptionDateDesc(
//                            doctorId
//                    );
//
//            List<PrescriptionResponse> response =
//                    prescriptions.stream()
//                            .map(prescription ->
//                                    modelMapper.map(
//                                            prescription,
//                                            PrescriptionResponse.class
//                                    )
//                            )
//                            .collect(Collectors.toList());
//
//            return new ApiResponse(
//                    1,
//                    "Doctor prescriptions fetched successfully",
//                    response,
//                    Long.valueOf(response.size())
//            );
//
//        } catch (Exception ex) {
//
//            log.error(
//                    "Error while fetching prescriptions for doctor ID: {}",
//                    doctorId,
//                    ex
//            );
//
//            return new ApiResponse(
//                    2,
//                    "Failed to fetch doctor prescriptions",
//                    null
//            );
//        }
//    }




    //response mapper
    private PrescriptionResponse buildPrescriptionResponse(
            Prescription prescription
    ) {

        PrescriptionResponse response =
                modelMapper.map(
                        prescription,
                        PrescriptionResponse.class
                );

        response.setPatientId(
                prescription.getPatient() != null
                        ? prescription.getPatient().getPatientId()
                        : null
        );

//        response.setDoctorId(
//                prescription.getDoctor() != null
//                        ? prescription.getDoctor().getStaffId()
//                        : null
//        );
//
//        response.setVisitId(
//                prescription.getVisit() != null
//                        ? prescription.getVisit().getVisitId()
//                        : null
//        );

        if (prescription.getItems() != null) {

            response.setItems(
                    prescription.getItems()
                            .stream()
                            .map(item ->
                                    modelMapper.map(
                                            item,
                                            PrescriptionItemResponse.class
                                    )
                            )
                            .toList()
            );
        }

        return response;
    }
}