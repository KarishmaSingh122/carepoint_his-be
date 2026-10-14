package com.suma.carepoint.services.auth;


import com.suma.carepoint.entities.auth.Role;
import com.suma.carepoint.entities.auth.User;
import com.suma.carepoint.entities.auth.UserRole;
import com.suma.carepoint.entities.auth.UserRoleId;
import com.suma.carepoint.exceptions.ConflictException;
import com.suma.carepoint.exceptions.ResourceNotFoundException;
import com.suma.carepoint.models.auth.UserRoleRequest;
import com.suma.carepoint.models.auth.UserRoleResponse;
import com.suma.carepoint.models.mapper.UserRoleMapper;
import com.suma.carepoint.repositories.auth.RoleRepository;
import com.suma.carepoint.repositories.auth.UserRepository;
import com.suma.carepoint.repositories.auth.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleMapper userRoleMapper;

    @Override
    public UserRoleResponse assignRole(UserRoleRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        if (!user.isActive()) {
            throw new ConflictException("Cannot assign role to an inactive user");
        }
        if (!role.isActive()) {
            throw new ConflictException("Cannot assign an inactive role");
        }
        if (userRoleRepository.existsByUserUserIdAndRoleRoleId(user.getUserId(), role.getRoleId())) {
            throw new ConflictException("Role is already assigned to user");
        }

        UserRole userRole = UserRole.builder()
                .id(UserRoleId.builder()
                        .userId(user.getUserId())
                        .roleId(role.getRoleId())
                        .build())
                .user(user).role(role)
                .build();
        try {
            UserRole saved = userRoleRepository.save(userRole);
            log.info("Role assigned successfully. userId={}, roleId={}", user.getUserId(), role.getRoleId());
            return userRoleMapper.toResponse(saved);
        } catch (DataIntegrityViolationException exception) {
            log.error("Role assignment failed. userId={}, roleId={}",
                    user.getUserId(), role.getRoleId(), exception);
            throw new ConflictException("Role is already assigned to user");
        }
    }

    @Override
    public UserRoleResponse getUserRole(Long userId, Long roleId) {
        UserRole userRole = userRoleRepository.findByUserUserIdAndRoleRoleId(userId, roleId)
                .orElseThrow(() -> new ResourceNotFoundException("User role assignment not found"));
        return userRoleMapper.toResponse(userRole);
    }

    @Override
    public List<UserRoleResponse> getRolesByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        return userRoleRepository.findByUserUserIdOrderByAssignedAtAsc(userId)
                .stream()
                .map(userRoleMapper::toResponse)
                .toList();
    }

    @Override
    public List<UserRoleResponse> getUsersByRole(Long roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new ResourceNotFoundException("Role not found");
        }
        return userRoleRepository.findByRoleRoleIdOrderByAssignedAtAsc(roleId)
                .stream()
                .map(userRoleMapper::toResponse)
                .toList();
    }

    @Override
    public void removeRole(Long userId, Long roleId) {

        UserRole userRole = userRoleRepository.findByUserUserIdAndRoleRoleId(userId, roleId)
                .orElseThrow(() -> new ResourceNotFoundException("User role assignment not found"));
        userRoleRepository.delete(userRole);
        log.info("Role removed successfully. userId={}, roleId={}", userId, roleId);
    }
}
