package com.suma.carepoint.services.auth;


import com.suma.carepoint.models.auth.UserRoleRequest;
import com.suma.carepoint.models.auth.UserRoleResponse;

import java.util.List;

public interface UserRoleService {

    UserRoleResponse assignRole(UserRoleRequest request);

    UserRoleResponse getUserRole(Long userId, Long roleId);

    List<UserRoleResponse> getRolesByUser(Long userId);

    List<UserRoleResponse> getUsersByRole(Long roleId);

    void removeRole(Long userId, Long roleId);
}
