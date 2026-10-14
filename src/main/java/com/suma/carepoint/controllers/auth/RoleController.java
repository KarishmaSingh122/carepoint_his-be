package com.suma.carepoint.controllers.auth;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.auth.RoleRequest;
import com.suma.carepoint.models.auth.RoleResponse;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.models.utility.PageResponse;
import com.suma.carepoint.services.auth.RoleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstant.Role.BASE)
@RequiredArgsConstructor
@Validated
public class RoleController {

    private final RoleService roleService;

    @PostMapping(ApiConstant.Role.CREATE)
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody RoleRequest request) {

        RoleResponse response = roleService.create(request);
        ApiResponse apiResponse = new ApiResponse(1, "", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping(ApiConstant.Role.GET_BY_ID)
    public ResponseEntity<ApiResponse> getById(@PathVariable @Min(1) Long roleId) {

        RoleResponse response = roleService.getById(roleId);
        return ResponseEntity.ok(new ApiResponse(1, "", response));
    }

    @GetMapping(ApiConstant.Role.GET_ALL)
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {

        PageResponse response = roleService.getAll(search, active, page, size);
        return ResponseEntity.ok(new ApiResponse(1, "", response));
    }

    @PutMapping(ApiConstant.Role.UPDATE)
    public ResponseEntity<ApiResponse> update(
            @PathVariable @Min(1) Long roleId,
            @Valid @RequestBody RoleRequest request) {

        RoleResponse response = roleService.update(roleId, request);
        return ResponseEntity.ok(new ApiResponse(1, "", response));
    }

    @PatchMapping(ApiConstant.Role.STATUS)
    public ResponseEntity<ApiResponse> updateStatus(
            @PathVariable @Min(1) Long roleId,
            @RequestParam boolean status) {

        RoleResponse response = roleService.updateStatus(roleId, status);
        return ResponseEntity.ok(new ApiResponse(1, "", response));
    }

    @DeleteMapping(ApiConstant.Role.DELETE)
    public ResponseEntity<ApiResponse> delete(@PathVariable @Min(1) Long roleId) {

        roleService.delete(roleId);
        return ResponseEntity.ok(new ApiResponse(1, "Role deleted successfully", null));
    }
}
