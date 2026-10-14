package com.suma.carepoint.controllers;


import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.models.medical_record.MedicalRecordRequest;
import com.suma.carepoint.models.medical_record.MedicalRecordResponse;
import com.suma.carepoint.models.utility.PageResponse;
import com.suma.carepoint.services.medical_record.MedicalRecordService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstant.MedicalRecord.BASE)
@RequiredArgsConstructor
@Validated
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @PostMapping(ApiConstant.MedicalRecord.CREATE)
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody MedicalRecordRequest request) {

        MedicalRecordResponse response = medicalRecordService.create(request);
        ApiResponse apiResponse = new ApiResponse(1, "Medical record created successfully", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping(ApiConstant.MedicalRecord.GET_BY_ID)
    public ResponseEntity<ApiResponse> getById(@PathVariable @Positive Long medicalRecordId) {
        MedicalRecordResponse response = medicalRecordService.getById(medicalRecordId);
        return ResponseEntity.ok(new ApiResponse(1, "", response));
    }

    @GetMapping(ApiConstant.MedicalRecord.GET_ALL)
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam(required = false) @Positive Long patientId,
            @RequestParam(required = false) @Positive Long visitId,
            @RequestParam(required = false) @Positive Long doctorId,
            @RequestParam(required = false) @Positive Long diagnosisId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {

        PageResponse response = medicalRecordService.getAll(patientId, visitId, doctorId, diagnosisId, page, size);
        return ResponseEntity.ok(new ApiResponse(1, "", response));
    }

    @PutMapping(ApiConstant.MedicalRecord.UPDATE)
    public ResponseEntity<ApiResponse> update(
            @PathVariable @Positive Long medicalRecordId,
            @Valid @RequestBody MedicalRecordRequest request) {

        MedicalRecordResponse response = medicalRecordService.update(medicalRecordId, request);
        return ResponseEntity.ok(new ApiResponse(1, "Medical record updated successfully", response));
    }

    @DeleteMapping(ApiConstant.MedicalRecord.DELETE)
    public ResponseEntity<ApiResponse> delete(@PathVariable @Positive Long medicalRecordId) {

        medicalRecordService.delete(medicalRecordId);
        return ResponseEntity.ok(
                new ApiResponse(1, "Medical record deleted successfully", null));
    }
}
