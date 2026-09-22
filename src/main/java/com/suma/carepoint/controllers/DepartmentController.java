package com.suma.carepoint.controllers;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.models.department.DepartmentRequest;
import com.suma.carepoint.models.department.DepartmentResponse;
import com.suma.carepoint.models.utility.PageResponse;
import com.suma.carepoint.services.department.DepartmentService;
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
@RequiredArgsConstructor
@Slf4j
@Validated
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping(ApiConstant.Department.CREATE)
    public ResponseEntity<ApiResponse> createDepartment(@Valid @RequestBody DepartmentRequest request) {

        log.info("Create department request received. departmentCode={}", request.getDepartmentCode());

        DepartmentResponse departmentResponse = departmentService.createDepartment(request);

        ApiResponse response = new ApiResponse(1, "", departmentResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(ApiConstant.Department.GET_BY_ID)
    public ResponseEntity<ApiResponse> getDepartmentById(@PathVariable @Min(value = 1, message = "Department ID must be positive") Long departmentId) {

        DepartmentResponse departmentResponse = departmentService.getDepartmentById(departmentId);

        ApiResponse response = new ApiResponse(1, "", departmentResponse);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping(ApiConstant.Department.GET_ALL)
    public ResponseEntity<ApiResponse> getDepartments(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page must be greater than or equal to 0") int page,
            @RequestParam(defaultValue = "20") @Min(value = 1, message = "Size must be greater than 0") @Max(value = 100, message = "Size must not exceed 100") int size) {

        PageResponse departmentPageResponse = departmentService.getDepartments(search, active, page, size);

        ApiResponse response = new ApiResponse(1, "", departmentPageResponse);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping(ApiConstant.Department.GET_ACTIVE)
    public ResponseEntity<ApiResponse> getActiveDepartments(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be greater than or equal to 0") int page,
            @RequestParam(defaultValue = "20") @Min(value = 1, message = "Size must be greater than 0")
            @Max(value = 100, message = "Size must not exceed 100") int size) {

        PageResponse departmentPageResponse = departmentService.getActiveDepartments(page, size);

        ApiResponse response = new ApiResponse(1, "", departmentPageResponse);

        return ResponseEntity.ok().body(response);
    }

    @PutMapping(ApiConstant.Department.UPDATE)
    public ResponseEntity<ApiResponse> updateDepartment(
            @PathVariable @Min(value = 1, message = "Department ID must be positive") Long departmentId,
            @Valid @RequestBody DepartmentRequest request) {

        DepartmentResponse departmentResponse = departmentService.updateDepartment(departmentId, request);
        ApiResponse response = new ApiResponse(1, "", departmentResponse);

        return ResponseEntity.ok().body(response);
    }

    @PatchMapping(ApiConstant.Department.UPDATE_STATUS)
    public ResponseEntity<ApiResponse> updateDepartmentStatus(
            @PathVariable @Min(value = 1, message = "Department ID must be positive") Long departmentId,
            @RequestParam boolean status) {

        DepartmentResponse departmentResponse = departmentService.updateDepartmentStatus(departmentId, status);
        ApiResponse response = new ApiResponse(1, "", departmentResponse);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(ApiConstant.Department.DELETE)
    public ResponseEntity<ApiResponse> deleteDepartment(
            @PathVariable @Min(value = 1, message = "Department ID must be positive") Long departmentId) {
        departmentService.deleteDepartment(departmentId);
        ApiResponse response = new ApiResponse(1, "Department deleted successfully", null);

        return ResponseEntity.ok().body(response);
    }
}
