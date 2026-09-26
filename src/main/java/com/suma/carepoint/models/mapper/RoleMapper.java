package com.suma.carepoint.models.mapper;

import com.suma.carepoint.entities.auth.Role;
import com.suma.carepoint.models.auth.RoleResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleMapper {

    private final ModelMapper modelMapper;

    public RoleResponse toResponse(Role role) {
        return modelMapper.map(role, RoleResponse.class);
    }
}
