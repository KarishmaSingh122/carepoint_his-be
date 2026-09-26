package com.suma.carepoint.services.auth;


import com.suma.carepoint.models.auth.RolePermissionRequest;
import com.suma.carepoint.models.auth.RolePermissionResponse;

import java.util.List;

public interface RolePermissionService {

    RolePermissionResponse assignPermission(RolePermissionRequest request);

    RolePermissionResponse getRolePermission(Long roleId, Long permissionId);

    List<RolePermissionResponse> getPermissionsByRole(Long roleId);

    List<RolePermissionResponse> getRolesByPermission(Long permissionId);

    void removePermission(Long roleId, Long permissionId);
}
