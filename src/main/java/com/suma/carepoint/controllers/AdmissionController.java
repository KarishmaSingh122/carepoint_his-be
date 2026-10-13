package com.suma.carepoint.controllers;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.admission.CreateAdmissionRequest;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.services.admission.AdmissionService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstant.Controller.HMIS + "/admission")

public class AdmissionController {

    private final AdmissionService admissionService;

    public AdmissionController(AdmissionService admissionService) {
        this.admissionService = admissionService;
    }

    @PostMapping(ApiConstant.Admission.CREATE)
    public ResponseEntity <ApiResponse>createAdmission(@RequestBody CreateAdmissionRequest request) {
        return ResponseEntity.ok().body( admissionService.createAdmission(request));
    }

    @GetMapping(ApiConstant.Admission.GET_BY_ID + "/{admissionId}")
    public ResponseEntity <ApiResponse> getAdmissionById(@PathVariable Long admissionId) {
        return ResponseEntity.ok().body (admissionService.getAdmissionById(admissionId));
    }

    @GetMapping("/treatment/{admissionId}/workspace")
    public ResponseEntity <ApiResponse> getPatientTreatmentWorkspaceByPatientId(@PathVariable Long admissionId) {
        return ResponseEntity.ok().body (admissionService.getPatientTreatmentWorkspaceByAdmissionId(admissionId));
    }

    @GetMapping(ApiConstant.Admission.PATIENT_SEARCH)
    public ResponseEntity<ApiResponse> searchPatientByKeyword(@RequestParam(name="keyword") String keyword) {
        return ResponseEntity.ok().body(admissionService.searchAdmissionByKeyword(keyword));
    }

    @GetMapping("/get-all/admit/patients")
    public ResponseEntity<ApiResponse> getAllAdmitPatients() {
        return ResponseEntity.ok().body (admissionService.getAllAdmitPatients());
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse> getAllAdmissions() {
        return ResponseEntity.ok().body (admissionService.getAllAdmissions());
    }

    @DeleteMapping("/delete/{admissionId}")
    public ResponseEntity<ApiResponse> deleteAdmissionById(@PathVariable Long admissionId) {
        return ResponseEntity.ok().body(admissionService.deleteAdmissionById(admissionId));
    }

    @PutMapping("/update/{admissionId}")
    public ResponseEntity<ApiResponse> updateAdmission(@PathVariable Long admissionId) {
        return ResponseEntity.ok().body(admissionService.updateAdmission(admissionId));
    }

    @GetMapping(ApiConstant.Admission.GET_BY_PATIENT_ID_OR_ABHA_ID)
    public ResponseEntity<ApiResponse> getAllAdmissions(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) String abhaId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {

        return ResponseEntity.ok(
                admissionService.getAllAdmissions(patientId, abhaId, page, size)
        );
    }
}

