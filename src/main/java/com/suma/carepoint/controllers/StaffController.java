package com.suma.carepoint.controllers;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.models.organization.StaffRequest;
import com.suma.carepoint.models.organization.StaffResponse;
import com.suma.carepoint.models.utility.PageResponse;
import com.suma.carepoint.services.organization.StaffService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstant.Staff.BASE)
@RequiredArgsConstructor
@Slf4j
@Validated
public class StaffController {

    private final StaffService staffService;

    @PostMapping(ApiConstant.Staff.CREATE)
    public ResponseEntity<ApiResponse> createStaff(@Valid @RequestBody StaffRequest request) {
        log.info("Create staff request received. employeeNo={}", request.getEmployeeNo());
        StaffResponse staffResponse = staffService.createStaff(request);
        ApiResponse response = new ApiResponse(1, "", staffResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(ApiConstant.Staff.GET_BY_ID)
    public ResponseEntity<ApiResponse> getStaffById(
            @PathVariable @Min(value = 1, message = "Staff ID must be positive") Long staffId) {
        StaffResponse staffResponse = staffService.getStaffById(staffId);
        ApiResponse response = new ApiResponse(1, "", staffResponse);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(ApiConstant.Staff.GET_ALL)
    public ResponseEntity<ApiResponse> getStaff(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) @Min(value = 1, message = "Department ID must be positive") Long departmentId,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page must be greater than or equal to 0") int page,
            @RequestParam(defaultValue = "20") @Min(value = 1, message = "Size must be greater than 0")
            @Max(value = 100, message = "Size must not exceed 100") int size) {
        PageResponse staffPageResponse = staffService.getStaff(search, active, departmentId, page, size);
        ApiResponse response = new ApiResponse(1, "", staffPageResponse);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(ApiConstant.Staff.GET_ACTIVE)
    public ResponseEntity<ApiResponse> getActiveStaff(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        PageResponse staffPageResponse = staffService.getActiveStaff(page, size);
        ApiResponse response = new ApiResponse(1, "", staffPageResponse);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(ApiConstant.Staff.GET_BY_DEPARTMENT)
    public ResponseEntity<ApiResponse> getStaffByDepartment(
            @RequestParam @Min(value = 1, message = "Department ID must be positive") Long departmentId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        PageResponse staffPageResponse = staffService.getStaffByDepartment(departmentId, page, size);
        ApiResponse response = new ApiResponse(1, "", staffPageResponse);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(ApiConstant.Staff.GET_BY_DESIGNATION)
    public ResponseEntity<ApiResponse> getStaffByDesignation(
            @RequestParam String designation,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        PageResponse staffPageResponse = staffService.getStaffByDesignation(designation, page, size);
        ApiResponse response = new ApiResponse(1, "", staffPageResponse);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping(ApiConstant.Staff.UPDATE)
    public ResponseEntity<ApiResponse> updateStaff(
            @PathVariable @Min(value = 1, message = "Staff ID must be positive") Long staffId,
            @Valid @RequestBody StaffRequest request) {
        StaffResponse staffResponse = staffService.updateStaff(staffId, request);
        ApiResponse response = new ApiResponse(1, "", staffResponse);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping(ApiConstant.Staff.UPDATE_STATUS)
    public ResponseEntity<ApiResponse> updateStaffStatus(
            @PathVariable @Min(value = 1, message = "Staff ID must be positive") Long staffId,
            @RequestParam boolean status) {
        StaffResponse staffResponse = staffService.updateStaffStatus(staffId, status);
        ApiResponse response = new ApiResponse(1, "", staffResponse);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(ApiConstant.Staff.DELETE)
    public ResponseEntity<ApiResponse> deleteStaff(
            @PathVariable @Min(value = 1, message = "Staff ID must be positive") Long staffId) {
        staffService.deleteStaff(staffId);
        ApiResponse response = new ApiResponse(1, "Staff deleted successfully", null);
        return ResponseEntity.ok().body(response);
    }
}
