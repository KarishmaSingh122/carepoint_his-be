package com.suma.carepoint.services.auth;

import com.suma.carepoint.entities.auth.Permission;
import com.suma.carepoint.exceptions.BadRequestException;
import com.suma.carepoint.exceptions.ConflictException;
import com.suma.carepoint.exceptions.ResourceNotFoundException;
import com.suma.carepoint.models.auth.PermissionRequest;
import com.suma.carepoint.models.auth.PermissionResponse;
import com.suma.carepoint.models.mapper.PermissionMapper;
import com.suma.carepoint.models.utility.PageResponse;
import com.suma.carepoint.repositories.auth.PermissionRepository;
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
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    public PermissionResponse create(PermissionRequest request) {

        String permissionName = normalizeRequired(request.getPermissionName());
        if (permissionRepository.existsByPermissionNameIgnoreCase(permissionName)) {
            throw new ConflictException("Permission name already exists");
        }

        Permission permission =
                Permission.builder()
                        .permissionName(permissionName)
                        .moduleName(normalize(request.getModuleName()))
                        .action(normalize(request.getAction()))
                        .description(normalize(request.getDescription()))
                        .build();
        try {
            Permission saved = permissionRepository.save(permission);
            log.info("Permission created successfully. permissionId={}, permissionName={}",
                    saved.getPermissionId(), saved.getPermissionName());
            return permissionMapper.toResponse(saved);
        } catch (DataIntegrityViolationException exception) {
            log.error("Permission creation failed due to database constraint. permissionName={}", permissionName, exception);
            throw new ConflictException("Permission name already exists");
        }
    }

    @Override
    public PermissionResponse getById(Long permissionId) {

        return permissionMapper.toResponse(findPermission(permissionId));
    }

    @Override
    public PageResponse getAll(
            String search, String moduleName,
            String action, int page, int size) {

        validatePagination(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "permissionName"));
        String normalizedSearch = normalize(search);
        String normalizedModule = normalize(moduleName);
        String normalizedAction = normalize(action);
        Page<Permission> permissions;

        if (normalizedModule != null && normalizedAction != null) {
            permissions = permissionRepository.
                    findByModuleNameContainingIgnoreCaseAndActionIgnoreCase(normalizedModule, normalizedAction, pageable);
        } else if (normalizedSearch != null) {
            permissions = permissionRepository.findByPermissionNameContainingIgnoreCase(normalizedSearch, pageable);
        } else if (normalizedModule != null) {
            permissions = permissionRepository.findByModuleNameContainingIgnoreCase(normalizedModule, pageable);
        } else if (normalizedAction != null) {
            permissions = permissionRepository.findByActionIgnoreCase(normalizedAction, pageable);
        } else {
            permissions = permissionRepository.findAll(pageable);
        }

        List<PermissionResponse> content =
                permissions.getContent()
                        .stream()
                        .map(permissionMapper::toResponse)
                        .toList();

        return PageResponse.builder()
                .content(content)
                .page(permissions.getNumber())
                .size(permissions.getSize())
                .totalElements(permissions.getTotalElements())
                .totalPages(permissions.getTotalPages())
                .first(permissions.isFirst())
                .last(permissions.isLast())
                .build();
    }

    @Override
    public PermissionResponse update(Long permissionId, PermissionRequest request) {

        Permission permission = findPermission(permissionId);
        String permissionName = normalizeRequired(request.getPermissionName());
        permissionRepository.findByPermissionNameIgnoreCase(permissionName).
                ifPresent(existingPermission -> {
                    if (!existingPermission.getPermissionId().equals(permissionId)) {
                        throw new ConflictException("Permission name already exists");
                    }
                });
        permission.setPermissionName(permissionName);
        permission.setModuleName(normalize(request.getModuleName()));
        permission.setAction(normalize(request.getAction()));
        permission.setDescription(normalize(request.getDescription()));
        try {
            Permission updated = permissionRepository.save(permission);
            log.info("Permission updated successfully. permissionId={}, permissionName={}",
                    updated.getPermissionId(), updated.getPermissionName());
            return permissionMapper.toResponse(updated);
        } catch (DataIntegrityViolationException exception) {
            log.error("Permission update failed due to database constraint. permissionId={}", permissionId, exception);
            throw new ConflictException("Permission name already exists");
        }
    }

    @Override
    public void delete(Long permissionId) {

        Permission permission = findPermission(permissionId);
        try {
            permissionRepository.delete(permission);
            log.info("Permission deleted successfully. permissionId={}", permissionId);
        } catch (DataIntegrityViolationException exception) {
            log.error("Permission deletion failed due to database constraint. permissionId={}", permissionId, exception);
            throw new ConflictException("Permission cannot be deleted because it is referenced by other records");
        }
    }

    private Permission findPermission(Long permissionId) {
        return permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim();
        return normalized.isBlank() ? null : normalized;
    }

    private String normalizeRequired(String value) {
        String normalized = normalize(value);
        if (normalized == null) {
            throw new BadRequestException("Permission name is required");
        }
        return normalized;
    }
}
