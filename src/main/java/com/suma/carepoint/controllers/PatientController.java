package com.suma.carepoint.controllers;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.models.patient.CreatePatientRequest;
import com.suma.carepoint.models.utility.PageResponse;
import com.suma.carepoint.security.annotation.HasPermissions;
import com.suma.carepoint.services.onehcx.abdm.OneHcxAbdmService;
import com.suma.carepoint.services.patient.PatientService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping(ApiConstant.Controller.HMIS)
@AllArgsConstructor
public class PatientController {
    @Autowired
    private PatientService patientService;

    private final OneHcxAbdmService oneHcxAbdmService;

    @HasPermissions("PATIENT_READ")
    @GetMapping(ApiConstant.Patient.GET_BY_PATIENT_ID)
    public ResponseEntity<ApiResponse> getPatientByPatientId(@RequestParam(name = "patientId") Long patientId) {
        return ResponseEntity.ok().body(patientService.getPatientByPatientId(patientId));
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @HasPermissions("PATIENT_READ")
    @GetMapping(ApiConstant.Patient.GET_BY_ABHA_ID)
    public ResponseEntity<ApiResponse> getPatientByAbhaId(@RequestParam String abhaId) {
        return ResponseEntity.ok().body(patientService.getPatientByAbhaId(abhaId));
    }

    @HasPermissions("PATIENT_READ")
    @GetMapping(ApiConstant.Patient.PATIENT_SEARCH)
    public ResponseEntity<ApiResponse> searchPatientByKeyword(@RequestParam(name = "keyword") String keyword) {
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
    @HasPermissions("PATIENT_CREATE")
    @PostMapping(ApiConstant.Patient.CREATE)
    public ResponseEntity<ApiResponse> createPatients(
            @RequestBody CreatePatientRequest createPatientRequest
    ) {
//        Map<String, MultipartFile> files = multipartHttpServletRequest.getFileMap();
//        files.remove("metadata");
        return ResponseEntity.ok().body(patientService.createPatients(createPatientRequest, null));
    }

    @HasPermissions("PATIENT_UPDATE")
    @PutMapping(ApiConstant.Patient.UPDATE + "/{patientId}")
    public ResponseEntity<ApiResponse> updatePatient(
            @PathVariable(name = "patientId") Long patientId,
            @RequestBody CreatePatientRequest request) {

        return ResponseEntity.ok().body(patientService.updatePatient(patientId, request));
    }

    @HasPermissions("PATIENT_DELETE")
    @DeleteMapping(ApiConstant.Patient.DELETE)
    public ResponseEntity<ApiResponse> DeletePatientByPatientId(
            @RequestParam(name = "patientId") Long patientId
    ) {
        return ResponseEntity.ok().body(patientService.DeletePatientByPatientId(patientId));
    }

    @HasPermissions("PATIENT_READ")
    @GetMapping(ApiConstant.Patient.GET)
    public ResponseEntity<ApiResponse> getAllPatients(
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {

        PageResponse response = patientService.getAllPatients(active, page, size);
        ApiResponse apiResponse = new ApiResponse(1, "", response);
        return ResponseEntity.ok().body(apiResponse);
    }

    @HasPermissions("PATIENT_READ")
    @GetMapping(ApiConstant.Patient.EXISTS)
    public ResponseEntity<ApiResponse> getPatientStatusIfExists(
            @RequestParam(name = "abhaId")
            @NotBlank @Size(min = 6, max = 15) String abhaId) {
        return ResponseEntity.ok().body(patientService.getPatientStatusIfExists(abhaId));
    }



    @PostMapping(ApiConstant.Patient.INITIATE_ABHA_VERIFICATION)
    public ResponseEntity<?> initiateAbhaVerification(@RequestBody Map<String, String> request) {

        String idNumber = request.get("idNumber");
        if (idNumber == null || idNumber.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "ABHA number is required"));
        }
        return ResponseEntity.ok(oneHcxAbdmService.initiateVerification(idNumber));

    }

    @PostMapping(ApiConstant.Patient.VERIFY_ABHA_OTP)
    public ResponseEntity<?> verifyAbhaOtp(@RequestBody Map<String, String> request) {

        String otp = request.get("otp");
        String txnId = request.get("txnId");
        if (otp == null || otp.isBlank() || txnId == null || txnId.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "OTP and transaction ID are required"));}
        return ResponseEntity.ok(oneHcxAbdmService.verifyOtp(otp, txnId));
    }

}
