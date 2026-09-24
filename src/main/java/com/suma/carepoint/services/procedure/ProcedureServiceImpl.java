package com.suma.carepoint.services.procedure;

import com.suma.carepoint.entities.admission.Admission;
import com.suma.carepoint.entities.organization.Staff;
import com.suma.carepoint.entities.procedure.PatientTreatment;
import com.suma.carepoint.entities.procedure.Procedure;
import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.procedure.PatientTreatmentRequest;
import com.suma.carepoint.models.procedure.PatientTreatmentResponse;
import com.suma.carepoint.models.procedure.ProcedureRequest;
import com.suma.carepoint.models.procedure.ProcedureResponse;
import com.suma.carepoint.repositories.admisssion.AdmissionRepository;
import com.suma.carepoint.repositories.organization.StaffRepository;
import com.suma.carepoint.repositories.procedure.PatientTreatmentRepository;
import com.suma.carepoint.repositories.procedure.ProcedureRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ProcedureServiceImpl implements ProcedureService {
    private final ModelMapper modelMapper;
    private final ProcedureRepository procedureRepository;
    private final PatientTreatmentRepository patientTreatmentRepository;
    private final StaffRepository staffRepository;
    private final AdmissionRepository admissionRepository;


    public ProcedureServiceImpl(ModelMapper modelMapper,
                                ProcedureRepository procedureRepository,
                                PatientTreatmentRepository patientTreatmentRepository,
                                StaffRepository staffRepository,
                                AdmissionRepository admissionRepository) {
        this.modelMapper = modelMapper;
        this.procedureRepository = procedureRepository;
        this.patientTreatmentRepository = patientTreatmentRepository;
        this.staffRepository = staffRepository;
        this.admissionRepository = admissionRepository;

    }


    /**
     * Create Procedure
     */
    @Override
    @Transactional
    public ApiResponse createProcedure(ProcedureRequest request) {

        try {

            if (request == null) {
                return new ApiResponse(
                        2,
                        "Procedure request cannot be null",
                        null
                );
            }

            String procedureCode = request.getProcedureCode()
                    .trim();

            String procedureName = request.getProcedureName()
                    .trim();


            // Check duplicate code
            if (procedureRepository
                    .existsByProcedureCodeIgnoreCase(procedureCode)) {

                return new ApiResponse(
                        2,
                        "Procedure code already exists",
                        null
                );
            }


            // Check duplicate name
            if (procedureRepository
                    .existsByProcedureNameIgnoreCase(procedureName)) {

                return new ApiResponse(
                        2,
                        "Procedure name already exists",
                        null
                );
            }


            Procedure procedure = modelMapper.map(
                    request,
                    Procedure.class
            );

            procedure.setProcedureCode(procedureCode);
            procedure.setProcedureName(procedureName);
            procedure.setActive(true);


            Procedure savedProcedure =
                    procedureRepository.save(procedure);


            ProcedureResponse response =
                    modelMapper.map(
                            savedProcedure,
                            ProcedureResponse.class
                    );


            return new ApiResponse(
                    1,
                    "Procedure created successfully",
                    response,
                    1L
            );

        } catch (DataIntegrityViolationException e) {

            log.error(
                    "Data integrity error while creating procedure",
                    e
            );

            return new ApiResponse(
                    2,
                    "Procedure code or name already exists",
                    null
            );

        } catch (Exception e) {

            log.error(
                    "Error while creating procedure",
                    e
            );

            return new ApiResponse(
                    2,
                    "Failed to create procedure",
                    null
            );
        }
    }


    /**
     * Get All Procedures
     */
    @Override
    @Transactional(readOnly = true)
    public ApiResponse getProcedures() {

        try {

            List<Procedure> procedures =
                    procedureRepository.findAll();

            List<ProcedureResponse> responses =
                    procedures.stream()
                            .map(procedure ->
                                    modelMapper.map(
                                            procedure,
                                            ProcedureResponse.class
                                    )
                            )
                            .toList();


            return new ApiResponse(
                    1,
                    "Procedures fetched successfully",
                    responses,
                    (long) responses.size()
            );

        } catch (Exception e) {

            log.error(
                    "Error while fetching procedures",
                    e
            );

            return new ApiResponse(
                    2,
                    "Failed to fetch procedures",
                    null
            );
        }
    }


    /**
     * Get Procedure By ID
     */
    @Override
    @Transactional(readOnly = true)
    public ApiResponse getProcedureById(Long procedureId) {

        try {

            Procedure procedure =
                    procedureRepository.findById(procedureId)
                            .orElse(null);

            if (procedure == null) {

                return new ApiResponse(
                        2,
                        "Procedure not found",
                        null
                );
            }


            ProcedureResponse response =
                    modelMapper.map(
                            procedure,
                            ProcedureResponse.class
                    );


            return new ApiResponse(
                    1,
                    "Procedure fetched successfully",
                    response,
                    1L
            );

        } catch (Exception e) {

            log.error(
                    "Error while fetching procedure: {}",
                    procedureId,
                    e
            );

            return new ApiResponse(
                    2,
                    "Failed to fetch procedure",
                    null
            );
        }
    }


    /**
     * Update Procedure
     */
    @Override
    @Transactional
    public ApiResponse updateProcedure(
            Long procedureId,
            ProcedureRequest request) {

        try {

            Procedure procedure =
                    procedureRepository.findById(procedureId)
                            .orElse(null);

            if (procedure == null) {

                return new ApiResponse(
                        2,
                        "Procedure not found",
                        null
                );
            }


            String procedureCode =
                    request.getProcedureCode().trim();

            String procedureName =
                    request.getProcedureName().trim();


            // Check duplicate code excluding current procedure
            if (procedureRepository
                    .existsByProcedureCodeIgnoreCaseAndProcedureIdNot(
                            procedureCode,
                            procedureId
                    )) {

                return new ApiResponse(
                        2,
                        "Procedure code already exists",
                        null
                );
            }


            // Check duplicate name excluding current procedure
            if (procedureRepository
                    .existsByProcedureNameIgnoreCaseAndProcedureIdNot(
                            procedureName,
                            procedureId
                    )) {

                return new ApiResponse(
                        2,
                        "Procedure name already exists",
                        null
                );
            }


            /*
             * Explicitly update only editable fields.
             *
             * Do not map the complete request because we don't
             * want to accidentally overwrite procedureId,
             * active, createdAt or updatedAt.
             */
            procedure.setProcedureCode(procedureCode);
            procedure.setProcedureName(procedureName);
            procedure.setDescription(request.getDescription());


            Procedure updatedProcedure =
                    procedureRepository.save(procedure);


            ProcedureResponse response =
                    modelMapper.map(
                            updatedProcedure,
                            ProcedureResponse.class
                    );


            return new ApiResponse(
                    1,
                    "Procedure updated successfully",
                    response,
                    1L
            );

        } catch (DataIntegrityViolationException e) {

            log.error(
                    "Data integrity error while updating procedure: {}",
                    procedureId,
                    e
            );

            return new ApiResponse(
                    2,
                    "Procedure code or name already exists",
                    null
            );

        } catch (Exception e) {

            log.error(
                    "Error while updating procedure: {}",
                    procedureId,
                    e
            );

            return new ApiResponse(
                    2,
                    "Failed to update procedure",
                    null
            );
        }
    }


    /**
     * Activate / Deactivate Procedure
     */
    @Override
    @Transactional
    public ApiResponse updateProcedureStatus(
            Long procedureId,
            Boolean active) {

        try {

            Procedure procedure =
                    procedureRepository.findById(procedureId)
                            .orElse(null);

            if (procedure == null) {

                return new ApiResponse(
                        2,
                        "Procedure not found",
                        null
                );
            }


            if (active == null) {

                return new ApiResponse(
                        2,
                        "Active status cannot be null",
                        null
                );
            }


            procedure.setActive(active);

            Procedure updatedProcedure =
                    procedureRepository.save(procedure);


            ProcedureResponse response =
                    modelMapper.map(
                            updatedProcedure,
                            ProcedureResponse.class
                    );


            String message = active
                    ? "Procedure activated successfully"
                    : "Procedure deactivated successfully";


            return new ApiResponse(
                    1,
                    message,
                    response,
                    1L
            );

        } catch (Exception e) {

            log.error(
                    "Error while updating procedure status: {}",
                    procedureId,
                    e
            );

            return new ApiResponse(
                    2,
                    "Failed to update procedure status",
                    null
            );
        }
    }


    /**
     * Delete Procedure
     */
    @Override
    @Transactional
    public ApiResponse deleteProcedure(Long procedureId) {

        try {

            Procedure procedure =
                    procedureRepository.findById(procedureId)
                            .orElse(null);

            if (procedure == null) {

                return new ApiResponse(
                        2,
                        "Procedure not found",
                        null
                );
            }


            /*
             * Don't physically delete a procedure if it has
             * already been used in patient treatment records.
             */
            boolean usedInTreatment =
                    patientTreatmentRepository
                            .existsByProcedureProcedureId(procedureId);


            if (usedInTreatment) {

                return new ApiResponse(
                        2,
                        "Procedure cannot be deleted because it is already used in patient treatments. Deactivate it instead.",
                        null
                );
            }


            procedureRepository.delete(procedure);


            return new ApiResponse(
                    1,
                    "Procedure deleted successfully",
                    null
            );

        } catch (DataIntegrityViolationException e) {

            log.error(
                    "Data integrity error while deleting procedure: {}",
                    procedureId,
                    e
            );

            return new ApiResponse(
                    2,
                    "Procedure cannot be deleted because it is being used",
                    null
            );

        } catch (Exception e) {

            log.error(
                    "Error while deleting procedure: {}",
                    procedureId,
                    e
            );

            return new ApiResponse(
                    2,
                    "Failed to delete procedure",
                    null
            );
        }
    }


    /**
     * Search Procedures
     */
    @Override
    @Transactional(readOnly = true)
    public ApiResponse searchProcedures(String keyword) {

        try {

            if (keyword == null || keyword.trim().isEmpty()) {

                return new ApiResponse(
                        2,
                        "Search keyword cannot be empty",
                        null
                );
            }


            String searchKeyword =
                    keyword.trim();


            List<Procedure> procedures =
                    procedureRepository
                            .findByProcedureNameContainingIgnoreCaseOrProcedureCodeContainingIgnoreCaseAndActiveTrue(
                                    searchKeyword,
                                    searchKeyword
                            );


            List<ProcedureResponse> responses =
                    procedures.stream()
                            .map(procedure ->
                                    modelMapper.map(
                                            procedure,
                                            ProcedureResponse.class
                                    )
                            )
                            .toList();


            return new ApiResponse(
                    1,
                    "Procedures searched successfully",
                    responses,
                    (long) responses.size()
            );

        } catch (Exception e) {

            log.error(
                    "Error while searching procedures: {}",
                    keyword,
                    e
            );

            return new ApiResponse(
                    2,
                    "Failed to search procedures",
                    null
            );
        }
    }


    //---------------------------Treatment

//---------------------------Treatment

    @Override
    @Transactional
    public ApiResponse createTreatment(
            PatientTreatmentRequest request) {

        try {

            if (request == null) {
                return new ApiResponse(
                        2,
                        "Treatment request cannot be null",
                        null
                );
            }

            /*
             * Admission is mandatory
             */
            Admission admission =
                    admissionRepository.findById(request.getAdmissionId())
                            .orElse(null);

            if (admission == null) {
                return new ApiResponse(
                        2,
                        "Admission not found",
                        null
                );
            }

            /*
             * Doctor is optional
             */
            Staff doctor = null;

            if (request.getDoctorId() != null) {

                doctor = staffRepository.findById(
                        request.getDoctorId()
                ).orElse(null);

                if (doctor == null) {
                    return new ApiResponse(
                            2,
                            "Doctor not found",
                            null
                    );
                }
            }

            /*
             * Procedure is optional
             */
            Procedure procedure = null;

            if (request.getProcedureId() != null) {

                procedure = procedureRepository.findById(
                        request.getProcedureId()
                ).orElse(null);

                if (procedure == null) {
                    return new ApiResponse(
                            2,
                            "Procedure not found",
                            null
                    );
                }

                if (!Boolean.TRUE.equals(procedure.getActive())) {
                    return new ApiResponse(
                            2,
                            "Selected procedure is inactive",
                            null
                    );
                }
            }

            PatientTreatment treatment =
                    new PatientTreatment();

            treatment.setAdmission(admission);
            treatment.setDoctor(doctor);
            treatment.setProcedure(procedure);

            treatment.setTreatmentDate(
                    request.getTreatmentDate() != null
                            ? request.getTreatmentDate()
                            : java.time.Instant.now()
            );

            treatment.setDescription(
                    request.getDescription()
            );

            treatment.setRemarks(
                    request.getRemarks()
            );

            PatientTreatment savedTreatment =
                    patientTreatmentRepository.save(treatment);

            PatientTreatmentResponse response =
                    buildTreatmentResponse(savedTreatment);

            return new ApiResponse(
                    1,
                    "Treatment recorded successfully",
                    response,
                    1L
            );

        } catch (Exception e) {

            log.error(
                    "Error while creating treatment",
                    e
            );

            return new ApiResponse(
                    2,
                    "Failed to record treatment",
                    null
            );
        }
    }


    @Override
    @Transactional(readOnly = true)
    public ApiResponse getTreatmentById(Long treatmentId) {

        try {

            if (treatmentId == null) {
                return new ApiResponse(
                        2,
                        "Treatment ID cannot be null",
                        null
                );
            }

            PatientTreatment treatment =
                    patientTreatmentRepository.findById(treatmentId)
                            .orElse(null);

            if (treatment == null) {
                return new ApiResponse(
                        2,
                        "Treatment not found",
                        null
                );
            }

            PatientTreatmentResponse response =
                    buildTreatmentResponse(treatment);

            return new ApiResponse(
                    1,
                    "Treatment fetched successfully",
                    response,
                    1L
            );

        } catch (Exception e) {

            log.error(
                    "Error while fetching treatment: {}",
                    treatmentId,
                    e
            );

            return new ApiResponse(
                    2,
                    "Failed to fetch treatment",
                    null
            );
        }
    }


    @Override
    @Transactional
    public ApiResponse updateTreatment(
            Long treatmentId,
            PatientTreatmentRequest request) {

        try {

            if (treatmentId == null) {
                return new ApiResponse(
                        2,
                        "Treatment ID cannot be null",
                        null
                );
            }

            if (request == null) {
                return new ApiResponse(
                        2,
                        "Treatment request cannot be null",
                        null
                );
            }

            /*
             * Find existing treatment
             */
            PatientTreatment treatment =
                    patientTreatmentRepository.findById(treatmentId)
                            .orElse(null);

            if (treatment == null) {
                return new ApiResponse(
                        2,
                        "Treatment not found",
                        null
                );
            }

            /*
             * Admission is mandatory
             */
            if (request.getAdmissionId() == null) {
                return new ApiResponse(
                        2,
                        "Admission ID is required",
                        null
                );
            }

            Admission admission =
                    admissionRepository.findById(
                            request.getAdmissionId()
                    ).orElse(null);

            if (admission == null) {
                return new ApiResponse(
                        2,
                        "Admission not found",
                        null
                );
            }

            /*
             * Doctor is optional
             */
            Staff doctor = null;

            if (request.getDoctorId() != null) {

                doctor = staffRepository.findById(
                        request.getDoctorId()
                ).orElse(null);

                if (doctor == null) {
                    return new ApiResponse(
                            2,
                            "Doctor not found",
                            null
                    );
                }
            }

            /*
             * Procedure is optional
             */
            Procedure procedure = null;

            if (request.getProcedureId() != null) {

                procedure = procedureRepository.findById(
                        request.getProcedureId()
                ).orElse(null);

                if (procedure == null) {
                    return new ApiResponse(
                            2,
                            "Procedure not found",
                            null
                    );
                }

                if (!Boolean.TRUE.equals(procedure.getActive())) {
                    return new ApiResponse(
                            2,
                            "Selected procedure is inactive",
                            null
                    );
                }
            }

            /*
             * Update treatment
             */
            treatment.setAdmission(admission);
            treatment.setDoctor(doctor);
            treatment.setProcedure(procedure);

            if (request.getTreatmentDate() != null) {
                treatment.setTreatmentDate(
                        request.getTreatmentDate()
                );
            }

            treatment.setDescription(
                    request.getDescription()
            );

            treatment.setRemarks(
                    request.getRemarks()
            );

            PatientTreatment updatedTreatment =
                    patientTreatmentRepository.save(treatment);

            PatientTreatmentResponse response =
                    buildTreatmentResponse(updatedTreatment);

            return new ApiResponse(
                    1,
                    "Treatment updated successfully",
                    response,
                    1L
            );

        } catch (Exception e) {

            log.error(
                    "Error while updating treatment: {}",
                    treatmentId,
                    e
            );

            return new ApiResponse(
                    2,
                    "Failed to update treatment",
                    null
            );
        }
    }


    @Override
    @Transactional
    public ApiResponse deleteTreatment(Long treatmentId) {

        try {

            if (treatmentId == null) {
                return new ApiResponse(
                        2,
                        "Treatment ID cannot be null",
                        null
                );
            }

            PatientTreatment treatment =
                    patientTreatmentRepository.findById(treatmentId)
                            .orElse(null);

            if (treatment == null) {
                return new ApiResponse(
                        2,
                        "Treatment not found",
                        null
                );
            }

            patientTreatmentRepository.delete(treatment);

            return new ApiResponse(
                    1,
                    "Treatment deleted successfully",
                    null
            );

        } catch (Exception e) {

            log.error(
                    "Error while deleting treatment: {}",
                    treatmentId,
                    e
            );

            return new ApiResponse(
                    2,
                    "Failed to delete treatment",
                    null
            );
        }
    }


    @Override
    @Transactional(readOnly = true)
    public ApiResponse getTreatmentsByAdmission(Long admissionId) {

        try {

            if (admissionId == null) {
                return new ApiResponse(
                        2,
                        "Admission ID cannot be null",
                        null
                );
            }

            /*
             * Verify admission exists
             */
            Admission admission =
                    admissionRepository.findById(admissionId)
                            .orElse(null);

            if (admission == null) {
                return new ApiResponse(
                        2,
                        "Admission not found",
                        null
                );
            }

            List<PatientTreatment> treatments =
                    patientTreatmentRepository
                            .findByAdmissionAdmissionIdOrderByTreatmentDateDesc(
                                    admissionId
                            );

            List<PatientTreatmentResponse> responses =
                    treatments.stream()
                            .map(this::buildTreatmentResponse)
                            .toList();

            return new ApiResponse(
                    1,
                    "Treatments fetched successfully",
                    responses,
                    (long) responses.size()
            );

        } catch (Exception e) {

            log.error(
                    "Error while fetching treatments for admission: {}",
                    admissionId,
                    e
            );

            return new ApiResponse(
                    2,
                    "Failed to fetch treatments",
                    null
            );
        }
    }


    @Override
    @Transactional(readOnly = true)
    public ApiResponse getTreatmentsByDoctor(Long doctorId) {

        try {

            if (doctorId == null) {
                return new ApiResponse(
                        2,
                        "Doctor ID cannot be null",
                        null
                );
            }

            /*
             * Verify doctor exists
             */
            Staff doctor =
                    staffRepository.findById(doctorId)
                            .orElse(null);

            if (doctor == null) {
                return new ApiResponse(
                        2,
                        "Doctor not found",
                        null
                );
            }

            List<PatientTreatment> treatments =
                    patientTreatmentRepository
                            .findByDoctorStaffIdOrderByTreatmentDateDesc(
                                    doctorId
                            );

            List<PatientTreatmentResponse> responses =
                    treatments.stream()
                            .map(this::buildTreatmentResponse)
                            .toList();

            return new ApiResponse(
                    1,
                    "Treatments fetched successfully",
                    responses,
                    (long) responses.size()
            );

        } catch (Exception e) {

            log.error(
                    "Error while fetching treatments for doctor: {}",
                    doctorId,
                    e
            );

            return new ApiResponse(
                    2,
                    "Failed to fetch treatments",
                    null
            );
        }
    }


    @Override
    @Transactional(readOnly = true)
    public ApiResponse getTreatmentsByProcedure(Long procedureId) {

        try {

            if (procedureId == null) {
                return new ApiResponse(
                        2,
                        "Procedure ID cannot be null",
                        null
                );
            }

            /*
             * Verify procedure exists
             */
            Procedure procedure =
                    procedureRepository.findById(procedureId)
                            .orElse(null);

            if (procedure == null) {
                return new ApiResponse(
                        2,
                        "Procedure not found",
                        null
                );
            }

            List<PatientTreatment> treatments =
                    patientTreatmentRepository
                            .findByProcedureProcedureIdOrderByTreatmentDateDesc(
                                    procedureId
                            );

            List<PatientTreatmentResponse> responses =
                    treatments.stream()
                            .map(this::buildTreatmentResponse)
                            .toList();

            return new ApiResponse(
                    1,
                    "Treatments fetched successfully",
                    responses,
                    (long) responses.size()
            );

        } catch (Exception e) {

            log.error(
                    "Error while fetching treatments for procedure: {}",
                    procedureId,
                    e
            );

            return new ApiResponse(
                    2,
                    "Failed to fetch treatments",
                    null
            );
        }
    }


    /**
     * Build Treatment Response
     * Explicit mapping is used here because PatientTreatment
     * contains nested Admission, Staff and Procedure objects.
     */
    private PatientTreatmentResponse buildTreatmentResponse(
            PatientTreatment treatment) {

        PatientTreatmentResponse response =
                new PatientTreatmentResponse();

        response.setTreatmentId(
                treatment.getTreatmentId()
        );

        response.setTreatmentDate(
                treatment.getTreatmentDate()
        );

        response.setDescription(
                treatment.getDescription()
        );

        response.setRemarks(
                treatment.getRemarks()
        );

        response.setCreatedAt(
                treatment.getCreatedAt()
        );

        response.setUpdatedAt(
                treatment.getUpdatedAt()
        );


        /*
         * Admission
         */
        if (treatment.getAdmission() != null) {

            response.setAdmissionId(
                    treatment.getAdmission().getAdmissionId()
            );
        }


        /*
         * Doctor
         */
        if (treatment.getDoctor() != null) {

            response.setDoctorId(
                    treatment.getDoctor().getStaffId()
            );

            /*
             * Change this according to your Staff entity.
             *
             * For example, if Staff has:
             * firstName + lastName
             */
            String firstName =
                    treatment.getDoctor().getFirstName();

            String lastName =
                    treatment.getDoctor().getLastName();

            if (firstName != null && lastName != null) {

                response.setDoctorName(
                        firstName + " " + lastName
                );

            } else if (firstName != null) {

                response.setDoctorName(firstName);

            } else {

                response.setDoctorName(lastName);
            }
        }


        /*
         * Procedure
         */
        if (treatment.getProcedure() != null) {

            response.setProcedureId(
                    treatment.getProcedure().getProcedureId()
            );

            response.setProcedureCode(
                    treatment.getProcedure().getProcedureCode()
            );

            response.setProcedureName(
                    treatment.getProcedure().getProcedureName()
            );
        }

        return response;
    }
}