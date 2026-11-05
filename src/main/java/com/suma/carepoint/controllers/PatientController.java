package com.suma.carepoint.controllers;

import com.suma.carepoint.entities.patient.PatientPolicyDetails;
import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.models.document.BulkDocumentItem;
import com.suma.carepoint.models.patient.CreatePatientRequest;
import com.suma.carepoint.models.patient.PatientResponse;
import com.suma.carepoint.models.patient.PatientUpdateRequest;
import com.suma.carepoint.models.utility.PageResponse;
import com.suma.carepoint.security.annotation.HasPermissions;
import com.suma.carepoint.services.document.DocumentService;
import com.suma.carepoint.services.onehcx.abdm.OneHcxAbdmService;
import com.suma.carepoint.services.patient.PatientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ApiConstant.Controller.HMIS)
@AllArgsConstructor
public class PatientController {

    @Autowired
    private PatientService patientService;

    private final OneHcxAbdmService oneHcxAbdmService;
    private final DocumentService documentService;
    private final ObjectMapper objectMapper;


    @HasPermissions("PATIENT_READ")
    @GetMapping(ApiConstant.Patient.GET_BY_PATIENT_ID)
    public ResponseEntity<ApiResponse> getPatientByPatientId(
            @RequestParam(name = "patientId") Long patientId) {
        return ResponseEntity.ok().body(patientService.getPatientByPatientId(patientId));
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @HasPermissions("PATIENT_READ")
    @GetMapping(ApiConstant.Patient.GET_BY_ABHA_ID)
    public ResponseEntity<ApiResponse> getPatientByAbhaId(@RequestParam String abhaId) {
        return ResponseEntity.ok(patientService.getPatientByAbhaId(abhaId));
    }

    @HasPermissions("PATIENT_READ")
    @GetMapping(ApiConstant.Patient.PATIENT_SEARCH)
    public ResponseEntity<ApiResponse> searchPatientByKeyword(@RequestParam(name = "keyword") String keyword) {
        return ResponseEntity.ok().body(patientService.searchPatientByKeyword(keyword));
    }

    @HasPermissions("PATIENT_CREATE")
    @PostMapping(value = ApiConstant.Patient.CREATE,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> createPatient(
            @Valid @RequestPart("patientDetails")
            CreatePatientRequest patientDetails,
            @RequestPart(value = "patientPolicyDetails", required = false)
            PatientPolicyDetails patientPolicyDetails,
            @RequestPart(value = "documentsJson", required = false)
            String documentsJson,
            @RequestParam Map<String, MultipartFile> files
    ) {

        List<BulkDocumentItem> documents = documentsJson != null
                ? objectMapper.readValue(documentsJson, objectMapper.getTypeFactory()
                .constructCollectionType(List.class, BulkDocumentItem.class)) : List.of();
        ApiResponse patient = patientService.createPatients(patientDetails, null);
        PatientResponse patientResponse = (PatientResponse) patient.getData();
        Map<String, MultipartFile> filteredFiles = files.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null
                        && !entry.getValue().isEmpty()
                        && !entry.getKey().equalsIgnoreCase("patientDetails")
                        && !entry.getKey().equalsIgnoreCase("patientPolicyDetails")
                        && !entry.getKey().equalsIgnoreCase("documentsJson")
                )
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        //skipping the policy details - need to store in DB.
        documentService.uploadDocuments(patientResponse.getPatientId(),null,null,documents, filteredFiles);
        return ResponseEntity.ok(patient);
    }

    @HasPermissions("PATIENT_UPDATE")
    @PutMapping(ApiConstant.Patient.UPDATE)
    public ResponseEntity<ApiResponse> updatePatient(
            @PathVariable(name = "patientId") Long patientId,
            @RequestBody PatientUpdateRequest request) {

        return ResponseEntity.ok(patientService.updatePatient(patientId, request));
    }

    @HasPermissions("PATIENT_DELETE")
    @DeleteMapping(ApiConstant.Patient.DELETE)
    public ResponseEntity<ApiResponse> deletePatientByPatientId(
            @RequestParam(name = "patientId") Long patientId) {

        return ResponseEntity.ok(patientService.DeletePatientByPatientId(patientId));
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
