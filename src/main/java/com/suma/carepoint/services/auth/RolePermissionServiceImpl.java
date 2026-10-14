package com.suma.carepoint.services.auth;


import com.suma.carepoint.entities.auth.Permission;
import com.suma.carepoint.entities.auth.Role;
import com.suma.carepoint.entities.auth.RolePermission;
import com.suma.carepoint.entities.auth.RolePermissionId;
import com.suma.carepoint.exceptions.ConflictException;
import com.suma.carepoint.exceptions.ResourceNotFoundException;
import com.suma.carepoint.models.auth.RolePermissionRequest;
import com.suma.carepoint.models.auth.RolePermissionResponse;
import com.suma.carepoint.models.mapper.RolePermissionMapper;
import com.suma.carepoint.repositories.auth.PermissionRepository;
import com.suma.carepoint.repositories.auth.RolePermissionRepository;
import com.suma.carepoint.repositories.auth.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RolePermissionServiceImpl implements RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionMapper rolePermissionMapper;

    @Override
    public RolePermissionResponse assignPermission(RolePermissionRequest request) {
        
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        Permission permission = permissionRepository.findById(request.getPermissionId())
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));
        if (!role.isActive()) {
            throw new ConflictException("Cannot assign permission to an inactive role");
        }
        if (rolePermissionRepository
                .existsByRoleRoleIdAndPermissionPermissionId(role.getRoleId(), permission.getPermissionId())) {
            throw new ConflictException("Permission is already assigned to role");
        }

        RolePermission rolePermission = RolePermission.builder()
                .id(RolePermissionId.builder()
                .roleId(role.getRoleId())
                .permissionId(permission.getPermissionId())
                .build())
                .role(role)
                .permission(permission)
                .build();
        try {
            RolePermission saved = rolePermissionRepository.save(rolePermission);
            log.info("Permission assigned successfully. roleId={}, permissionId={}",
                    role.getRoleId(), permission.getPermissionId());
            return rolePermissionMapper.toResponse(saved);
        } catch (DataIntegrityViolationException exception) {
            log.error("Permission assignment failed. roleId={}, permissionId={}",
                    role.getRoleId(), permission.getPermissionId(), exception);
            throw new ConflictException("Permission is already assigned to role");
        }
    }

    @Override
    public RolePermissionResponse getRolePermission(Long roleId, Long permissionId) {

        RolePermission rolePermission = rolePermissionRepository.findByRoleRoleIdAndPermissionPermissionId(roleId, permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Role permission assignment not found"));
        return rolePermissionMapper.toResponse(rolePermission);
    }

    @Override
    public List<RolePermissionResponse> getPermissionsByRole(Long roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new ResourceNotFoundException("Role not found");
        }
        return rolePermissionRepository.findByRoleRoleIdOrderByPermissionPermissionNameAsc(roleId)
                .stream().map(rolePermissionMapper::toResponse).toList();
    }

    @Override
    public List<RolePermissionResponse> getRolesByPermission(Long permissionId) {
        if (!permissionRepository.existsById(permissionId)) {
            throw new ResourceNotFoundException("Permission not found");
        }
        return rolePermissionRepository.findByPermissionPermissionIdOrderByRoleRoleNameAsc(permissionId)
                .stream().map(rolePermissionMapper::toResponse).toList();
    }

    @Override
    public void removePermission(Long roleId, Long permissionId) {

        RolePermission rolePermission = rolePermissionRepository.findByRoleRoleIdAndPermissionPermissionId(roleId, permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Role permission assignment not found"));
        rolePermissionRepository.delete(rolePermission);
        log.info("Permission removed successfully. roleId={}, permissionId={}", roleId, permissionId);
    }
}
