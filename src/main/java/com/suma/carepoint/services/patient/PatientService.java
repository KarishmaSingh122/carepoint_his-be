package com.suma.carepoint.services.patient;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.patient.CreatePatientRequest;
import com.suma.carepoint.models.utility.PageResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface PatientService {
     ApiResponse getPatientByPatientId(Long patientId);

     ApiResponse getPatientByAbhaId(String abhaId);

     ApiResponse createPatients(CreatePatientRequest createPatientRequest, Map<String, MultipartFile> files);

     ApiResponse updatePatient(Long patientId, CreatePatientRequest request);

     ApiResponse DeletePatientByPatientId(Long patientId);

     PageResponse getAllPatients(Boolean active, int page, int size);

     ApiResponse searchPatientByKeyword(String keyword);

     ApiResponse getPatientStatusIfExists(String abhaId);
}
