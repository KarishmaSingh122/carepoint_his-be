package com.suma.carepoint.models.mapper;

import com.suma.carepoint.entities.auth.Permission;
import com.suma.carepoint.models.auth.PermissionResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PermissionMapper {

    private final ModelMapper modelMapper;

    public PermissionResponse toResponse(Permission permission) {
        return modelMapper.map(permission, PermissionResponse.class);
    }
}
