package com.suma.carepoint.services.auth;

import com.suma.carepoint.entities.auth.Role;
import com.suma.carepoint.exceptions.ConflictException;
import com.suma.carepoint.exceptions.ResourceNotFoundException;
import com.suma.carepoint.models.auth.RoleRequest;
import com.suma.carepoint.models.auth.RoleResponse;
import com.suma.carepoint.models.mapper.RoleMapper;
import com.suma.carepoint.models.utility.PageResponse;
import com.suma.carepoint.repositories.auth.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.suma.carepoint.models.utility.PageResponse.validatePagination;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleResponse create(RoleRequest request) {

        String roleName = request.getRoleName().trim();
        if (roleRepository.existsByRoleNameIgnoreCase(roleName)) {
            throw new ConflictException("Role name already exists");
        }

        Role role = Role.builder()
                .roleName(roleName)
                .description(normalizeDescription(request.getDescription()))
                .active(true)
                .build();
        try {
            Role saved = roleRepository.save(role);
            log.info("Role created successfully. roleId={}, roleName={}", saved.getRoleId(), saved.getRoleName());
            return roleMapper.toResponse(saved);
        } catch (DataIntegrityViolationException exception) {
            log.error("Role creation failed due to database constraint. roleName={}", roleName, exception);
            throw new ConflictException("Role name already exists");
        }
    }

    @Override
    public RoleResponse getById(Long roleId) {
        return roleMapper.toResponse(findRole(roleId));
    }

    @Override
    public PageResponse getAll(String search, Boolean active, int page, int size) {

        validatePagination(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "roleName"));
        String normalizedSearch = search == null ? null : search.trim();
        Page<Role> roles;

        if (normalizedSearch != null && !normalizedSearch.isBlank() && active != null) {
            roles = roleRepository.findByActiveAndRoleNameContainingIgnoreCase(
                    active, normalizedSearch, pageable);
        } else if (normalizedSearch != null && !normalizedSearch.isBlank()) {
            roles = roleRepository.findByRoleNameContainingIgnoreCase(normalizedSearch, pageable);
        } else if (active != null) {
            roles = roleRepository.findByActive(active, pageable);
        } else {
            roles = roleRepository.findAll(pageable);
        }

        List<RoleResponse> content = roles.getContent()
                .stream()
                .map(roleMapper::toResponse)
                .toList();

        return PageResponse.builder()
                .content(content)
                .page(roles.getNumber())
                .size(roles.getSize())
                .totalElements(roles.getTotalElements())
                .totalPages(roles.getTotalPages())
                .first(roles.isFirst())
                .last(roles.isLast())
                .build();
    }

    @Override
    public RoleResponse update(Long roleId, RoleRequest request) {

        Role role = findRole(roleId);
        String roleName = request.getRoleName().trim();
        roleRepository.findByRoleNameIgnoreCase(roleName)
                .ifPresent(existingRole -> {
                    if (!existingRole.getRoleId().equals(roleId)) {
                        throw new ConflictException("Role name already exists");
                    }
                });
        role.setRoleName(roleName);
        role.setDescription(normalizeDescription(request.getDescription()));
        try {
            Role updated = roleRepository.save(role);
            log.info("Role updated successfully. roleId={}, roleName={}", updated.getRoleId(), updated.getRoleName());
            return roleMapper.toResponse(updated);
        } catch (DataIntegrityViolationException exception) {
            log.error("Role update failed due to database constraint. roleId={}", roleId, exception);
            throw new ConflictException("Role name already exists");
        }
    }

    @Override
    public RoleResponse updateStatus(Long roleId, boolean active) {

        Role role = findRole(roleId);
        role.setActive(active);
        Role updated = roleRepository.save(role);
        log.info("Role status updated. roleId={}, active={}", roleId, active);
        return roleMapper.toResponse(updated);
    }

    @Override
    public void delete(Long roleId) {
        Role role = findRole(roleId);
        try {
            roleRepository.delete(role);
            log.info("Role deleted successfully. roleId={}", roleId);
        } catch (DataIntegrityViolationException exception) {
            log.error("Role deletion failed due to database constraint. roleId={}", roleId, exception);
            throw new ConflictException("Role cannot be deleted because it is referenced by other records");
        }
    }

    private Role findRole(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    private String normalizeDescription(String description) {
        if (description == null) {
            return null;
        }
        String normalized = description.trim();
        return normalized.isBlank() ? null : normalized;
    }

}
