package com.suma.carepoint.services.admission;

import com.suma.carepoint.entities.admission.Admission;
import com.suma.carepoint.entities.bedassignment.BedAssignment;
import com.suma.carepoint.entities.organization.Staff;
import com.suma.carepoint.entities.patient.Patient;
import com.suma.carepoint.entities.procedure.PatientTreatment;
import com.suma.carepoint.entities.visit.Visit;
import com.suma.carepoint.entities.ward.Ward;
import com.suma.carepoint.exceptions.ResourceNotFoundException;
import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.admission.AdmissionResponse;
import com.suma.carepoint.models.admission.CreateAdmissionRequest;
import com.suma.carepoint.models.medication.PrescriptionRequest;
import com.suma.carepoint.models.medication.PrescriptionResponse;
import com.suma.carepoint.models.patient.PatientResponse;
import com.suma.carepoint.models.procedure.PatientTreatmentResponse;
import com.suma.carepoint.models.treatment_workspace.TreatmentWorkspaceAdmissionResponse;
import com.suma.carepoint.models.treatment_workspace.TreatmentWorkspacePatientResponse;
import com.suma.carepoint.models.treatment_workspace.TreatmentWorkspaceResponse;
import com.suma.carepoint.models.treatment_workspace.TreatmentWorkspaceTreatmentResponse;
import com.suma.carepoint.repositories.admisssion.AdmissionRepository;
import com.suma.carepoint.repositories.bed_assignment.BedAssignmentRepository;
import com.suma.carepoint.repositories.organization.StaffRepository;
import com.suma.carepoint.repositories.patient.PatientRepository;
import com.suma.carepoint.repositories.procedure.PatientTreatmentRepository;
import com.suma.carepoint.repositories.ward.WardRepository;
import com.suma.carepoint.repositories.wardroom.WardRoomRepository;
import com.suma.carepoint.services.medication.MedicationService;
import com.suma.carepoint.services.procedure.ProcedureService;
import com.suma.carepoint.services.visit.VisitService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class AdmissionServiceImpl implements AdmissionService {

    private final AdmissionRepository admissionRepository;
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private final StaffRepository staffRepository;
    private final ProcedureService procedureService;
    private final VisitService visitService;
    private final BedAssignmentRepository bedAssignmentRepository;
    private final MedicationService medicationService;

    public AdmissionServiceImpl(AdmissionRepository admissionRepository, PatientRepository patientRepository,
                                ModelMapper modelMapper,
                                StaffRepository staffRepository,
                                ProcedureService procedureService,
                                VisitService visitService,
                                BedAssignmentRepository bedAssignmentRepository,
                                MedicationService medicationService) {
        this.admissionRepository = admissionRepository;
        this.patientRepository = patientRepository;
        this.modelMapper = modelMapper;
        this.staffRepository = staffRepository;
        this.procedureService = procedureService;
        this.visitService =visitService;
        this.bedAssignmentRepository = bedAssignmentRepository;
        this.medicationService = medicationService;
    }

    @Override
    public ApiResponse createAdmission(CreateAdmissionRequest request) {
        try {

            Patient patient = patientRepository.findById(request.getPatientId()).orElseThrow(() ->
                    new RuntimeException("Patient not found with id: " + request.getPatientId()));
            Staff staff = staffRepository.findById(request.getAdmittingDoctorId()).orElseThrow(() ->
                    new RuntimeException("Doctor  not found with id: " + request.getAdmittingDoctorId()));

            Admission admission = new Admission();

            admission.setAdmissionNumber(generateAdmissionNo(request.getPatientId()));
            admission.setPatient(patient);
            admission.setAdmissionDate(request.getAdmissionDate());
            admission.setAdmissionType(request.getAdmissionType());
            admission.setAdmittingDoctor(staff);
            admission.setStatus(request.getStatus());
            admission.setReason(request.getReason());

            Admission savedAdmission = admissionRepository.save(admission);

            AdmissionResponse response = new AdmissionResponse();

            response.setAdmissionId(savedAdmission.getAdmissionId());
            response.setAdmissionNumber(savedAdmission.getAdmissionNumber());
            response.setPatientId(savedAdmission.getPatient().getPatientId());
            response.setAdmissionDate(savedAdmission.getAdmissionDate());
            response.setAdmissionType(savedAdmission.getAdmissionType());
            response.setStatus(savedAdmission.getStatus());
            response.setReason(savedAdmission.getReason());

            return new ApiResponse(1, "", response);

        } catch (Exception e) {
            log.error("Error occurred while creating admission", e);
            return new ApiResponse(2, "", null);
        }
    }

    @Override
    public ApiResponse getAdmissionById(Long admissionId) {
        AdmissionResponse admissionResponse = null;

        try {

            Admission admission = admissionRepository.findById(admissionId).orElseThrow(() -> new RuntimeException("Admission not found"));

            admissionResponse = modelMapper.map(admission, AdmissionResponse.class);

            ApiResponse response = new ApiResponse(1, "", admissionResponse);

            return response;

        } catch (Exception e) {

            log.error("Error occurred in getAdmissionById: {}, error: {}", admissionId, e.getMessage());
            return new ApiResponse(2, "", admissionResponse);
        }
    }


    public AdmissionResponse getAdmissionByPatientId(Long pateintId) {
        AdmissionResponse admissionResponse = null;

        try {

            Admission admission = admissionRepository.findByPatientPatientId(pateintId).orElseThrow(() -> new RuntimeException("Admission not found"));

            admissionResponse = modelMapper.map(admission, AdmissionResponse.class);
            if (admission.getPatient() != null) {
                admissionResponse.setPatientId(admission.getPatient().getPatientId());
                admissionResponse.setPatientName(admission.getPatient().getFirstName() + " " + admission.getPatient().getLastName());
            }

            if (admission.getAdmittingDoctor() != null) {
                admissionResponse.setAdmittingDoctorId(admission.getAdmittingDoctor().getStaffId());
                admissionResponse.setAdmittingDoctorName(admission.getAdmittingDoctor().getFirstName() + " " + admission.getAdmittingDoctor().getLastName());
            }

            admissionResponse.setAdmissionDate(admission.getAdmissionDate());
            admissionResponse.setAdmissionType(admission.getAdmissionType());
            admissionResponse.setStatus(admission.getStatus());
            admissionResponse.setReason(admission.getReason());
            return admissionResponse;
        } catch (Exception e) {

            log.error("Error occurred in getAdmissionById: {}, error: {}", pateintId, e.getMessage());
            return null;
        }
    }

    @Override
    public ApiResponse getAllAdmissions() {

        try {

            List<Admission> admissions = admissionRepository.findAll();

            List<AdmissionResponse> admissionResponses = admissions.stream()
                    .map(admission -> {

                        AdmissionResponse response = new AdmissionResponse();

                        response.setAdmissionId(admission.getAdmissionId());
                        response.setAdmissionNumber(admission.getAdmissionNumber());

                        if (admission.getPatient() != null) {
                            response.setPatientId(admission.getPatient().getPatientId());
                            response.setPatientName(admission.getPatient().getFirstName() + " " + admission.getPatient().getLastName());
                        }

                        if (admission.getAdmittingDoctor() != null) {
                            response.setAdmittingDoctorId(admission.getAdmittingDoctor().getStaffId());
                            response.setAdmittingDoctorName(admission.getAdmittingDoctor().getFirstName() + " " + admission.getAdmittingDoctor().getLastName());
                        }

                        response.setAdmissionDate(admission.getAdmissionDate());
                        response.setAdmissionType(admission.getAdmissionType());
                        response.setStatus(admission.getStatus());
                        response.setReason(admission.getReason());

                        return response;
                    }).toList();

            return new ApiResponse(
                    1,
                    "",
                    admissionResponses,
                    (long) admissionResponses.size()
            );

        } catch (Exception e) {
            log.error("Error occurred while getting all admissions: {}", e.getMessage());
            return new ApiResponse(2, "", null);
        }
    }

    @Override
    public ApiResponse deleteAdmissionById(Long admissionId) {

        AdmissionResponse admissionResponse = null;

        try {

            Admission admission = admissionRepository.findById(admissionId).orElseThrow(() ->
                    new RuntimeException("Admission not found"));

            admissionResponse = modelMapper.map(admission, AdmissionResponse.class);

            admissionRepository.delete(admission);

            ApiResponse response = new ApiResponse(1, "Admission deleted successfully", admissionResponse);

            return response;

        } catch (Exception e) {

            log.error("Error occurred while deleting admission: {}, error: {}", admissionId, e.getMessage());

            return new ApiResponse(2, "", admissionResponse);
        }
    }


    @Override
    public ApiResponse updateAdmission(Long admissionId) {
        return null;
    }

    @Override
    public ApiResponse getAllAdmitPatients() {
        List<PatientResponse> patientResponseList = new ArrayList<>();
        patientResponseList = admissionRepository.findAdmittedPatients().stream().map(patient ->
                modelMapper.map(patient, PatientResponse.class)).toList();

        return new ApiResponse(1, "All Admitted Patients get successfully", patientResponseList);
    }

//    @Override
//    public ApiResponse getPatientTreatmentWorkspaceByPatientId(Long patientId) {
//
//        try {
//            // 1. Validate patient
//            Patient patient = patientRepository.findById(patientId)
//                    .orElseThrow(() ->
//                            new ResourceNotFoundException(
//                                    "Patient not found with id: " + patientId
//                            )
//                    );
//            Visit visit =  visitService.getVisitByPatientId(patientId);
//
//            // 2. Get admission for patient
//            AdmissionResponse admissionResponse =
//                    getAdmissionByPatientId(patient.getPatientId());
//
//            if (admissionResponse == null) {
//
//                return ApiResponse.builder()
//                        .status(1)
//                        .message("Patient found but no admission found")
//                        .data(
//                                TreatmentWorkspaceResponse.builder()
//                                        .patient(buildPatientWorkspaceResponse(patient))
//                                        .admission(null)
//                                        .treatments(Collections.emptyList())
//                                        .build()
//                        )
//                        .build();
//            }
//
//            // 3. Get treatments using admission id
//            ApiResponse treatmentApiResponse =
//                    procedureService.getTreatmentsByAdmission(
//                            admissionResponse.getAdmissionId()
//                    );
//
//            List<PatientTreatmentResponse> patientTreatments =
//                    Collections.emptyList();
//
//            if (treatmentApiResponse != null
//                    && treatmentApiResponse.getStatus() == 1
//                    && treatmentApiResponse.getData() != null) {
//
//                patientTreatments =
//                        (List<PatientTreatmentResponse>)
//                                treatmentApiResponse.getData();
//            }
//
//            // 4. Map patient
//            TreatmentWorkspacePatientResponse patientResponse =
//                    buildPatientWorkspaceResponse(patient);
//
//            // 5. Map admission
//            TreatmentWorkspaceAdmissionResponse workspaceAdmission =
//                    modelMapper.map(
//                            admissionResponse,
//                            TreatmentWorkspaceAdmissionResponse.class
//                    );
//            BedAssignment  bedAssignment = bedAssignmentRepository.findByAdmissionAdmissionId(workspaceAdmission.getAdmissionId());
//            workspaceAdmission.setAdmissionDate(admissionResponse.getAdmissionDate());
//            workspaceAdmission.setDepartmentId(visit.getDepartment().getDepartmentId());
//            workspaceAdmission.setDepartmentName(visit.getDepartment().getDepartmentName());
//            workspaceAdmission.setBedId(bedAssignment.getBed().getBedId());
//            workspaceAdmission.setBedNumber(bedAssignment.getBed().getBedNumber());
//            workspaceAdmission.setRoomId(bedAssignment.getBed().getRoom().getRoomId());
//            workspaceAdmission.setRoomNumber(bedAssignment.getBed().getRoom().getRoomNumber());
//            workspaceAdmission.setWardId(bedAssignment.getBed().getRoom().getWard().getWardId());
//            workspaceAdmission.setWardName(bedAssignment.getBed().getRoom().getWard().getWardName());
//
//            // 6. Map treatments
//            List<TreatmentWorkspaceTreatmentResponse> treatments =
//                    patientTreatments.stream()
//                            .map(treatment ->
//                                    modelMapper.map(
//                                            treatment,
//                                            TreatmentWorkspaceTreatmentResponse.class
//                                    )
//                            )
//                            .toList();
//
//            //prescriptions
//            ApiResponse prescriptionResponse = medicationService.getPatientPrescriptions(patientId);
//
//            // 7. Build workspace
//            TreatmentWorkspaceResponse workspace =
//                    TreatmentWorkspaceResponse.builder()
//                            .patient(patientResponse)
//                            .admission(workspaceAdmission)
//                            .treatments(treatments)
//                            .prescriptions(prescriptions)
//                            .build();
//
//            // 8. Return standard API response
//            return ApiResponse.builder()
//                    .status(1)
//                    .message("Patient treatment workspace fetched successfully")
//                    .data(workspace)
//                    .build();
//
//        } catch (ResourceNotFoundException e) {
//            log.error(
//                    "Patient treatment workspace not found for patientId: {}",
//                    patientId,
//                    e
//            );
//
//            return ApiResponse.builder()
//                    .status(2)
//                    .message(e.getMessage())
//                    .data(null)
//                    .build();
//
//        } catch (Exception e) {
//            log.error(
//                    "Error while fetching treatment workspace for patientId: {}",
//                    patientId,
//                    e
//            );
//
//            return ApiResponse.builder()
//                    .status(2)
//                    .message("Failed to fetch patient treatment workspace")
//                    .data(null)
//                    .build();
//        }
//    }

    @Override
    public ApiResponse getPatientTreatmentWorkspaceByPatientId(Long patientId) {

        try {

            // 1. Validate patient
            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Patient not found with id: " + patientId
                            )
                    );

            Visit visit = visitService.getVisitByPatientId(patientId);

            // 2. Get admission for patient
            AdmissionResponse admissionResponse =
                    getAdmissionByPatientId(patient.getPatientId());

            /*
             * Patient exists but does not have an admission.
             * We can still return patient information and empty
             * treatment/prescription lists.
             */
            if (admissionResponse == null) {

                return ApiResponse.builder()
                        .status(1)
                        .message("Patient found but no admission found")
                        .data(
                                TreatmentWorkspaceResponse.builder()
                                        .patient(
                                                buildPatientWorkspaceResponse(patient)
                                        )
                                        .admission(null)
                                        .treatments(Collections.emptyList())
                                        .prescriptions(Collections.emptyList())
                                        .build()
                        )
                        .build();
            }

            // 3. Get treatments using admission ID
            ApiResponse treatmentApiResponse =
                    procedureService.getTreatmentsByAdmission(
                            admissionResponse.getAdmissionId()
                    );

            List<PatientTreatmentResponse> patientTreatments =
                    Collections.emptyList();

            if (treatmentApiResponse != null
                    && treatmentApiResponse.getStatus() == 1
                    && treatmentApiResponse.getData() != null) {

                patientTreatments =
                        (List<PatientTreatmentResponse>)
                                treatmentApiResponse.getData();
            }

            // 4. Get prescriptions using patient ID
            ApiResponse prescriptionApiResponse =
                    medicationService.getPatientPrescriptions(patientId);

            List<PrescriptionResponse> prescriptions =
                    Collections.emptyList();

            if (prescriptionApiResponse != null
                    && prescriptionApiResponse.getStatus() == 1
                    && prescriptionApiResponse.getData() != null) {

                prescriptions =
                        (List<PrescriptionResponse>)
                                prescriptionApiResponse.getData();
            }

            // 5. Map patient
            TreatmentWorkspacePatientResponse patientResponse =
                    buildPatientWorkspaceResponse(patient);

            // 6. Map admission
            TreatmentWorkspaceAdmissionResponse workspaceAdmission =
                    modelMapper.map(
                            admissionResponse,
                            TreatmentWorkspaceAdmissionResponse.class
                    );

            BedAssignment bedAssignment =
                    bedAssignmentRepository
                            .findByAdmissionAdmissionId(
                                    workspaceAdmission.getAdmissionId()
                            );

            workspaceAdmission.setAdmissionDate(
                    admissionResponse.getAdmissionDate()
            );

            if (visit != null && visit.getDepartment() != null) {

                workspaceAdmission.setDepartmentId(
                        visit.getDepartment().getDepartmentId()
                );

                workspaceAdmission.setDepartmentName(
                        visit.getDepartment().getDepartmentName()
                );
            }

            if (bedAssignment != null
                    && bedAssignment.getBed() != null) {

                workspaceAdmission.setBedId(
                        bedAssignment.getBed().getBedId()
                );

                workspaceAdmission.setBedNumber(
                        bedAssignment.getBed().getBedNumber()
                );

                if (bedAssignment.getBed().getRoom() != null) {

                    workspaceAdmission.setRoomId(
                            bedAssignment.getBed()
                                    .getRoom()
                                    .getRoomId()
                    );

                    workspaceAdmission.setRoomNumber(
                            bedAssignment.getBed()
                                    .getRoom()
                                    .getRoomNumber()
                    );

                    if (bedAssignment.getBed()
                            .getRoom()
                            .getWard() != null) {

                        workspaceAdmission.setWardId(
                                bedAssignment.getBed()
                                        .getRoom()
                                        .getWard()
                                        .getWardId()
                        );

                        workspaceAdmission.setWardName(
                                bedAssignment.getBed()
                                        .getRoom()
                                        .getWard()
                                        .getWardName()
                        );
                    }
                }
            }

            // 7. Map treatments
            List<TreatmentWorkspaceTreatmentResponse> treatments =
                    patientTreatments.stream()
                            .map(treatment ->
                                    modelMapper.map(
                                            treatment,
                                            TreatmentWorkspaceTreatmentResponse.class
                                    )
                            )
                            .toList();

            // 8. Build workspace
            TreatmentWorkspaceResponse workspace =
                    TreatmentWorkspaceResponse.builder()
                            .patient(patientResponse)
                            .admission(workspaceAdmission)
                            .treatments(treatments)
                            .prescriptions(prescriptions)
                            .build();

            // 9. Return standard API response
            return ApiResponse.builder()
                    .status(1)
                    .message(
                            "Patient treatment workspace fetched successfully"
                    )
                    .data(workspace)
                    .build();

        } catch (ResourceNotFoundException e) {

            log.error(
                    "Patient treatment workspace not found for patientId: {}",
                    patientId,
                    e
            );

            return ApiResponse.builder()
                    .status(2)
                    .message(e.getMessage())
                    .data(null)
                    .build();

        } catch (Exception e) {

            log.error(
                    "Error while fetching treatment workspace for patientId: {}",
                    patientId,
                    e
            );

            return ApiResponse.builder()
                    .status(2)
                    .message("Failed to fetch patient treatment workspace")
                    .data(null)
                    .build();
        }
    }
    private TreatmentWorkspacePatientResponse buildPatientWorkspaceResponse(
            Patient patient) {

        return TreatmentWorkspacePatientResponse.builder()
                .patientId(patient.getPatientId())
                .patientName(
                        Stream.of(
                                        patient.getFirstName(),
                                        patient.getLastName()
                                )
                                .filter(Objects::nonNull)
                                .filter(name -> !name.isBlank())
                                .collect(Collectors.joining(" "))
                )
                .gender(String.valueOf(patient.getGender()))
                .dateOfBirth(patient.getDateOfBirth())
                .mobileNo(patient.getPhone())
                .build();
    }

    //create admission no system generated
    private String generateAdmissionNo(Long patientId) {
        String timestamp = LocalDateTime.now().format(formatter);
        return "ADM-" + timestamp + "-" + patientId;

    }

}

