package com.suma.carepoint.controllers;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.models.emr.DiagnosisRequest;
import com.suma.carepoint.services.emr.DiagnosisService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstant.Controller.DIAGNOSIS)
@AllArgsConstructor
public class DiagnosisController {

    @Autowired
    private final DiagnosisService diagnosisService;

    //apis related to procedures

    @PostMapping(ApiConstant.Diagnosis.DIAGNOSIS)
    public ResponseEntity<ApiResponse> createDiagnosis(@Valid @RequestBody DiagnosisRequest request) {
        return ResponseEntity.ok().body(diagnosisService.createDiagnosis(request));
    }

    @GetMapping(ApiConstant.Diagnosis.DIAGNOSIS)
    public ResponseEntity<ApiResponse> getDiagnosises() {
        return ResponseEntity.ok().body(diagnosisService.getDiagnosises());
    }


    /**
     * Get Procedure By ID
     */
    @GetMapping(ApiConstant.Diagnosis.DIAGNOSIS + "/{diagnosisId}")
    public ResponseEntity<ApiResponse> getDiagnosisById(
            @PathVariable("diagnosisId") Long diagnosisId) {

        return ResponseEntity.ok()
                .body(diagnosisService.getDiagnosisById(diagnosisId));
    }


    /**
     * Update Procedure
     * PUT /api/procedures/{diagnosisId}
     */
    @PutMapping(ApiConstant.Diagnosis.DIAGNOSIS + "/{diagnosisId}")
    public ResponseEntity<ApiResponse> updateDiagnosis(
            @PathVariable("diagnosisId") Long diagnosisId,
            @Valid @RequestBody DiagnosisRequest request) {
        return ResponseEntity.ok().body(diagnosisService.updateDiagnosis(diagnosisId, request));
    }


    /**
     * Activate / Deactivate Procedure
     */
    @PatchMapping(
            ApiConstant.Diagnosis.DIAGNOSIS
                    + "/{diagnosisId}/status"
    )
    public ResponseEntity<ApiResponse> updateDiagnosisStatus(
            @PathVariable("diagnosisId") Long diagnosisId,
            @RequestParam("active") Boolean active) {

        return ResponseEntity.ok().body(diagnosisService.updateDiagnosisStatus(
                        diagnosisId,
                        active
                ));
    }


    /**
     * Delete Procedure
     */
    @DeleteMapping(
            ApiConstant.Diagnosis.DIAGNOSIS
                    + "/{diagnosisId}"
    )
    public ResponseEntity<ApiResponse> deleteDiagnosis(
            @PathVariable("diagnosisId") Long diagnosisId) {

        return ResponseEntity.ok().body(diagnosisService.deleteDiagnosis(diagnosisId));
    }


    /**
     * Search Procedures
     */
    @GetMapping(ApiConstant.Diagnosis.DIAGNOSIS + "/search")
    public ResponseEntity<ApiResponse> searchDiagnosises(
            @RequestParam("keyword") String keyword) {

        return ResponseEntity.ok().body(diagnosisService.searchDiagnosises(keyword));
    }



}
