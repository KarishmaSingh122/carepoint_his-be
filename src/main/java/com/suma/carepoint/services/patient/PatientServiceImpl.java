package com.suma.carepoint.services.patient;

import com.suma.carepoint.entities.patient.Patient;
import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.patient.CreatePatientRequest;
import com.suma.carepoint.models.patient.PatientResponse;
import com.suma.carepoint.repositories.patient.PatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PatientServiceImpl implements PatientService{

    private  final PatientRepository patientRepository;

    private final ModelMapper modelMapper;

    public PatientServiceImpl( PatientRepository patientRepository, ModelMapper modelMapper){
        this.patientRepository= patientRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApiResponse getPatientByPatientId(Long patientId) {
        PatientResponse patientResponse = null;
        try {
            Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found"));
            patientResponse = modelMapper.map(patient,PatientResponse.class);
            ApiResponse response = new ApiResponse(1, "", List.of(patientResponse));
            return response ;
        } catch (Exception e) {
            log.error("Error occurred in getPatients by abhaId: {}, error: {}", patientId, e.getMessage());
            return new ApiResponse(2, "", patientResponse
            );
        }
    }

    @Override
    public ApiResponse getPatientByAbhaId(String abhaId) {
        PatientResponse patientResponse = null;
        try {
            Patient patient = patientRepository.findByAbhaId(abhaId).orElseThrow(() -> new RuntimeException("Patient not found"));
            patientResponse = modelMapper.map(patient,PatientResponse.class);
            ApiResponse response = new ApiResponse(1, "", patientResponse);
            return response ;
        } catch (Exception e) {
            log.error("Error occurred in getPatients by abhaId: {}, error: {}", abhaId, e.getMessage());
            return new ApiResponse(2, "", patientResponse
            );
        }
    }

    @Override
    public ApiResponse createPatients(CreatePatientRequest createPatientRequest, Map<String, MultipartFile> files) {

        Patient patient = modelMapper.map(createPatientRequest,Patient.class);

        Patient savedPatient =patientRepository.save(patient);


        PatientResponse patientResponse = modelMapper.map(savedPatient,PatientResponse.class);
        ApiResponse response = new ApiResponse(1, "", patientResponse);
        return response;
    }

    @Override
    public ApiResponse updatePatient(Long patientId, CreatePatientRequest request) {
        PatientResponse patientResponse = null;
        try {
            Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found"));

            patient.setCity(request.getCity());
            patient.setAddress(request.getAddress());
            patient.setEmail(request.getEmail());
            patient.setBloodGroup(request.getBloodGroup());
            patient.setDateOfBirth(request.getDateOfBirth());
            patient.setEmergencyContactName(request.getEmergencyContactName());
            patient.setEmergencyContactPhone(request.getEmergencyContactPhone());
            patient.setFirstName(request.getFirstName());
            patient = patientRepository.save(patient);
            patientResponse = modelMapper.map(patient,PatientResponse.class);
            ApiResponse response = new ApiResponse(1, "", patientResponse);
            return response ;
        } catch (Exception e) {
            log.error("Error occurred in update pateint  by patientId: {}, error: {}", patientId, e.getMessage());
            return new ApiResponse(2, "", patientResponse
            );
        }     }

    @Override
    public ApiResponse DeletePatientByPatientId(Long patientId) {
        PatientResponse patientResponse = null;
        try {
            Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found"));
            patient.setActive(false);
            patient = patientRepository.save(patient);
            patientResponse = modelMapper.map(patient,PatientResponse.class);

            ApiResponse response = new ApiResponse(1, "", patientResponse);
            return response ;
        } catch (Exception e) {
            log.error("Error occurred in getPatients by abhaId: {}, error: {}", patientId, e.getMessage());
            return new ApiResponse(2, "", patientResponse
            );
        }       }

    @Override
    public ApiResponse searchPatientByKeyword(String keyword) {

        // Validate keyword
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ApiResponse(
                    2,
                    "Search keyword is required",
                    null
            );
        }

        String searchKeyword = keyword.trim();

        // Optional: prevent unnecessarily short searches
        if (searchKeyword.length() < 2) {
            return new ApiResponse(
                    2,
                    "Search keyword must contain at least 2 characters",
                    null
            );
        }

        try {

            List<Patient> patients =
                    patientRepository.searchByKeyword(searchKeyword);

            if (patients == null || patients.isEmpty()) {
                return new ApiResponse(
                        1,
                        "No patients found",
                        Collections.emptyList()
                );
            }

            List<PatientResponse> patientResponses = patients.stream()
                    .map(patient ->
                            modelMapper.map(patient, PatientResponse.class)
                    )
                    .toList();

            return new ApiResponse(
                    1,
                    "Patients found successfully",
                    patientResponses,
                    (long)patientResponses.size()
            );

        } catch (Exception e) {

            log.error(
                    "Error while searching patients with keyword: {}",
                    searchKeyword,
                    e
            );

            return new ApiResponse(
                    2,
                    "Unable to search patients",
                    null
            );
        }
    }
}
