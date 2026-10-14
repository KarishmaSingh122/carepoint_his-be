package com.suma.carepoint.models.mapper;

import com.suma.carepoint.entities.auth.UserRole;
import com.suma.carepoint.models.auth.UserRoleResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRoleMapper {

    private final ModelMapper modelMapper;

    public UserRoleResponse toResponse(UserRole userRole) {

        UserRoleResponse response = modelMapper.map(userRole, UserRoleResponse.class);
        response.setUserId(userRole.getUser().getUserId());
        response.setUsername(userRole.getUser().getUsername());
        response.setRoleId(userRole.getRole().getRoleId());
        response.setRoleName(userRole.getRole().getRoleName());
        response.setAssignedAt(userRole.getAssignedAt());

        return response;
    }
}
