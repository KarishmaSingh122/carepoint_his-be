package com.suma.carepoint.services.emr;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.emr.DiagnosisRequest;
import jakarta.validation.Valid;

public interface DiagnosisService {
     ApiResponse createDiagnosis(@Valid DiagnosisRequest request);

     ApiResponse getDiagnosises();

     ApiResponse getDiagnosisById(Long diagnosisId);

     ApiResponse updateDiagnosis(Long diagnosisId, @Valid DiagnosisRequest request);

     ApiResponse updateDiagnosisStatus(Long diagnosisId, Boolean active);

     ApiResponse deleteDiagnosis(Long diagnosisId);

     ApiResponse searchDiagnoses(String keyword);
}
