package com.suma.carepoint.services.admission;


import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.admission.CreateAdmissionRequest;

public interface AdmissionService {
    ApiResponse createAdmission(CreateAdmissionRequest createAdmissionRequest);

    ApiResponse getAdmissionById(Long admissionId);


    ApiResponse getAllAdmissions();

    ApiResponse deleteAdmissionById(Long admissionId);

    ApiResponse updateAdmission(Long admissionId);
}
