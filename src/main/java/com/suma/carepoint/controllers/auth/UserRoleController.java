package com.suma.carepoint.controllers.auth;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.auth.UserRoleRequest;
import com.suma.carepoint.models.auth.UserRoleResponse;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.services.auth.UserRoleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstant.UserRole.BASE)
@RequiredArgsConstructor
@Validated
public class UserRoleController {

    private final UserRoleService userRoleService;

    @PostMapping(ApiConstant.UserRole.ASSIGN)
    public ResponseEntity<ApiResponse> assignRole(@Valid @RequestBody UserRoleRequest request) {

        UserRoleResponse response = userRoleService.assignRole(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse(1, "Role assigned successfully", response));
    }

    @GetMapping(ApiConstant.UserRole.GET_BY_USER)
    public ResponseEntity<ApiResponse> getRolesByUser(@PathVariable @Positive Long userId) {

        List<UserRoleResponse> response = userRoleService.getRolesByUser(userId);
        return ResponseEntity.ok(new ApiResponse(1, "", response));
    }

    @GetMapping(ApiConstant.UserRole.GET_BY_USER_AND_ROLE)
    public ResponseEntity<ApiResponse> getUserRole(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long roleId) {

        UserRoleResponse response = userRoleService.getUserRole(userId, roleId);
        return ResponseEntity.ok(new ApiResponse(1, "", response));
    }

    @GetMapping(ApiConstant.UserRole.GET_BY_ROLE)
    public ResponseEntity<ApiResponse> getUsersByRole(@PathVariable @Positive Long roleId) {

        List<UserRoleResponse> response = userRoleService.getUsersByRole(roleId);
        return ResponseEntity.ok(new ApiResponse(1, "", response));
    }

    @DeleteMapping(ApiConstant.UserRole.REMOVE)
    public ResponseEntity<ApiResponse> removeRole(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long roleId) {

        userRoleService.removeRole(userId, roleId);
        return ResponseEntity.ok(
                new ApiResponse(1, "Role removed successfully", null));
    }
}
