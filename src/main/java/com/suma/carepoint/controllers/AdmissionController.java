package com.suma.carepoint.controllers;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.admission.CreateAdmissionRequest;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.services.admission.AdmissionService;
import com.suma.carepoint.services.admission.AdmissionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(ApiConstant.Controller.HMIS + "/admission")

public class AdmissionController {

    private final AdmissionService admissionService;


    public AdmissionController(AdmissionService admissionService) {
        this.admissionService = admissionService;
    }


    @PostMapping(ApiConstant.Admission.CREATE)
    public ApiResponse createAdmission(@RequestBody CreateAdmissionRequest request) {

        return admissionService.createAdmission(request);
    }

    @GetMapping(ApiConstant.Admission.GET_BY_ID + "/{admissionId}")
    public ApiResponse getAdmissionById(
            @PathVariable Long admissionId) {

        return admissionService.getAdmissionById(admissionId);
    }

    @GetMapping("/get-all")
    public ApiResponse getAllAdmissions() {

        return admissionService.getAllAdmissions();
    }

    @DeleteMapping("/delete/{admissionId}")
    public ApiResponse deleteAdmissionById(
            @PathVariable Long admissionId) {

        return admissionService.deleteAdmissionById(admissionId);
    }

    @PutMapping("/update/{admissionId}")
    public ApiResponse updateAdmission(
            @PathVariable Long admissionId) {

        return admissionService.updateAdmission(admissionId);
    }
}

