package com.suma.carepoint.controllers;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.models.patient.CreatePatientRequest;
import com.suma.carepoint.models.utility.PageResponse;
import com.suma.carepoint.services.patient.PatientService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstant.Controller.HMIS)
@AllArgsConstructor
public class PatientController {
    @Autowired
    private PatientService patientService;

    @GetMapping(ApiConstant.Patient.GET_BY_PATIENT_ID)
    public ResponseEntity<ApiResponse> getPatientByPatientId(@RequestParam(name = "patientId") Long patientId) {
        return ResponseEntity.ok().body(patientService.getPatientByPatientId(patientId));
    }

    @GetMapping(ApiConstant.Patient.GET_BY_ABHA_ID)
    public ResponseEntity<ApiResponse> getPatientByAbhaId(@RequestParam String abhaId) {
        return ResponseEntity.ok().body(patientService.getPatientByAbhaId(abhaId));
    }

    @GetMapping(ApiConstant.Patient.SEARCH)
    public ResponseEntity<ApiResponse> searchPatientByKeyword(@RequestParam(name="keyword") String keyword) {
        return ResponseEntity.ok().body(patientService.searchPatientByKeyword(keyword));
    }


//    @PostMapping(ApiConstant.Patient.CREATE)
//    public ResponseEntity<ApiResponse> createPatients(
//            @RequestPart("metadata") CreatePatientRequest createPatientRequest,
//            MultipartHttpServletRequest multipartHttpServletRequest
//    ) {
//        Map<String, MultipartFile> files = multipartHttpServletRequest.getFileMap();
//        files.remove("metadata");
//        return ResponseEntity.ok().body(patientService.createPatients(createPatientRequest, files));
//    }

    @PostMapping(ApiConstant.Patient.CREATE)
    public ResponseEntity<ApiResponse> createPatients(
            @RequestBody CreatePatientRequest createPatientRequest
    ) {
//        Map<String, MultipartFile> files = multipartHttpServletRequest.getFileMap();
//        files.remove("metadata");
        return ResponseEntity.ok().body(patientService.createPatients(createPatientRequest, null));
    }

    @PutMapping(ApiConstant.Patient.UPDATE+"/{patientId}")
    public ResponseEntity<ApiResponse> updatePatient(
            @PathVariable (name = "patientId") Long patientId,
            @RequestBody CreatePatientRequest request) {

        return ResponseEntity.ok().body(patientService.updatePatient(patientId, request));
    }

    @DeleteMapping(ApiConstant.Patient.DELETE)
    public ResponseEntity<ApiResponse> DeletePatientByPatientId(
            @RequestParam(name = "patientId") Long patientId
    ) {
        return ResponseEntity.ok().body(patientService.DeletePatientByPatientId(patientId));
    }

    @GetMapping(ApiConstant.Patient.GET)
    public ResponseEntity<ApiResponse> getAllPatients(
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {

        PageResponse response = patientService.getAllPatients(active, page, size);
        ApiResponse apiResponse = new ApiResponse(1, "", response);
        return ResponseEntity.ok().body(apiResponse);
    }
}
