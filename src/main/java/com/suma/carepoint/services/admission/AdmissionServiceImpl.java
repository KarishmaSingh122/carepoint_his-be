    package com.suma.carepoint.services.admission;
    import com.suma.carepoint.entities.admission.Admission;
    import com.suma.carepoint.entities.patient.Patient;
    import com.suma.carepoint.models.ApiResponse;
    import com.suma.carepoint.models.admission.AdmissionResponse;
    import com.suma.carepoint.models.admission.CreateAdmissionRequest;
    import com.suma.carepoint.repositories.admisssion.AdmissionRepository;
    import com.suma.carepoint.repositories.patient.PatientRepository;
    import lombok.extern.slf4j.Slf4j;
    import org.modelmapper.ModelMapper;
    import org.springframework.stereotype.Service;
    import java.util.List;

    @Slf4j
    @Service
    public class AdmissionServiceImpl implements AdmissionService {

        private final AdmissionRepository admissionRepository;
        private final PatientRepository patientRepository;
        private final ModelMapper modelMapper;

        public AdmissionServiceImpl(AdmissionRepository admissionRepository, PatientRepository patientRepository,
                                    ModelMapper modelMapper) {
            this.admissionRepository = admissionRepository;
            this.patientRepository = patientRepository;
            this.modelMapper = modelMapper;
        }

        @Override
        public ApiResponse createAdmission(CreateAdmissionRequest request) {
            try {

                Patient patient = patientRepository.findById(request.getPatientId()).orElseThrow(() ->
                                new RuntimeException("Patient not found with id: " + request.getPatientId()));

                Admission admission = new Admission();

                admission.setAdmissionNumber(request.getAdmissionNumber());
                admission.setPatient(patient);
                admission.setAdmissionDate(request.getAdmissionDate());
                admission.setAdmissionType(request.getAdmissionType());
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

    }

