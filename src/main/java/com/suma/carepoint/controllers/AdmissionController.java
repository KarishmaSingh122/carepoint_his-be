package com.suma.carepoint.controllers;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.admission.CreateAdmissionRequest;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.services.admission.AdmissionService;
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
}

