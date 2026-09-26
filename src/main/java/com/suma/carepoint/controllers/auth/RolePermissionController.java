package com.suma.carepoint.controllers.auth;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.auth.RolePermissionRequest;
import com.suma.carepoint.models.auth.RolePermissionResponse;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.services.auth.RolePermissionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstant.RolePermission.BASE)
@RequiredArgsConstructor
@Validated
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    @PostMapping(ApiConstant.RolePermission.ASSIGN)
    public ResponseEntity<ApiResponse> assignPermission(
            @Valid @RequestBody RolePermissionRequest request) {

        RolePermissionResponse response = rolePermissionService.assignPermission(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(1, "Permission assigned successfully", response));
    }

    @GetMapping(ApiConstant.RolePermission.GET_BY_ROLE)
    public ResponseEntity<ApiResponse> getPermissionsByRole(@PathVariable @Positive Long roleId) {

        List<RolePermissionResponse> response = rolePermissionService.getPermissionsByRole(roleId);
        return ResponseEntity.ok(new ApiResponse(1, "", response));
    }

    @GetMapping(ApiConstant.RolePermission.GET_BY_ROLE_AND_PERMISSION)
    public ResponseEntity<ApiResponse> getRolePermission(
            @PathVariable @Positive Long roleId,
            @PathVariable @Positive Long permissionId) {
        RolePermissionResponse response = rolePermissionService.getRolePermission(roleId, permissionId);
        return ResponseEntity.ok(new ApiResponse(1, "", response));
    }

    @GetMapping(ApiConstant.RolePermission.GET_BY_PERMISSION)
    public ResponseEntity<ApiResponse> getRolesByPermission(@PathVariable @Positive Long permissionId) {

        List<RolePermissionResponse> response = rolePermissionService.getRolesByPermission(permissionId);
        return ResponseEntity.ok(new ApiResponse(1, "", response));
    }

    @DeleteMapping(ApiConstant.RolePermission.REMOVE)
    public ResponseEntity<ApiResponse> removePermission(
            @PathVariable @Positive Long roleId,
            @PathVariable @Positive Long permissionId) {

        rolePermissionService.removePermission(roleId, permissionId);
        return ResponseEntity.ok(
                new ApiResponse(1, "Permission removed successfully", null));
    }
}
