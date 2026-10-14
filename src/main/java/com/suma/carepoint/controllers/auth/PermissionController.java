package com.suma.carepoint.controllers.auth;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.auth.PermissionRequest;
import com.suma.carepoint.models.auth.PermissionResponse;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.models.utility.PageResponse;
import com.suma.carepoint.services.auth.PermissionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstant.Permission.BASE)
@RequiredArgsConstructor
@Validated
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping(ApiConstant.Permission.CREATE)
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody PermissionRequest request) {

        PermissionResponse response = permissionService.create(request);
        ApiResponse apiResponse = new ApiResponse(1, "", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping(ApiConstant.Permission.GET_BY_ID)
    public ResponseEntity<ApiResponse> getById(@PathVariable @Min(1) Long permissionId) {

        PermissionResponse response = permissionService.getById(permissionId);
        return ResponseEntity.ok(new ApiResponse(1, "", response));
    }

    @GetMapping(ApiConstant.Permission.GET_ALL)
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String moduleName,
            @RequestParam(required = false) String action,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {

        PageResponse response = permissionService.getAll(search, moduleName, action, page, size);
        return ResponseEntity.ok(new ApiResponse(1, "", response));
    }

    @PutMapping(ApiConstant.Permission.UPDATE)
    public ResponseEntity<ApiResponse> update(
            @PathVariable @Min(1) Long permissionId,
            @Valid @RequestBody PermissionRequest request) {
        PermissionResponse response = permissionService.update(permissionId, request);
        return ResponseEntity.ok(new ApiResponse(1, "", response));
    }

    @DeleteMapping(ApiConstant.Permission.DELETE)
    public ResponseEntity<ApiResponse> delete(@PathVariable @Min(1) Long permissionId) {

        permissionService.delete(permissionId);
        return ResponseEntity.ok(
                new ApiResponse(1, "Permission deleted successfully", null));
    }
}
