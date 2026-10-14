package com.suma.carepoint.models.mapper;

import com.suma.carepoint.entities.auth.RolePermission;
import com.suma.carepoint.models.auth.RolePermissionResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RolePermissionMapper {

    private final ModelMapper modelMapper;

    public RolePermissionResponse toResponse(RolePermission rolePermission) {

        RolePermissionResponse response = modelMapper.map(rolePermission, RolePermissionResponse.class);
        response.setRoleId(rolePermission.getRole().getRoleId());
        response.setRoleName(rolePermission.getRole().getRoleName());
        response.setPermissionId(rolePermission.getPermission().getPermissionId());
        response.setPermissionName(rolePermission.getPermission().getPermissionName());
        response.setModuleName(rolePermission.getPermission().getModuleName());
        response.setAction(rolePermission.getPermission().getAction());

        return response;
    }
}
