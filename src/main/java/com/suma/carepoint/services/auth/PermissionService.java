package com.suma.carepoint.services.auth;


import com.suma.carepoint.models.auth.PermissionRequest;
import com.suma.carepoint.models.auth.PermissionResponse;
import com.suma.carepoint.models.utility.PageResponse;

public interface PermissionService {

    PermissionResponse create(PermissionRequest request);

    PermissionResponse getById(Long permissionId);

    PageResponse getAll(String search, String moduleName, String action, int page, int size);

    PermissionResponse update(Long permissionId, PermissionRequest request);

    void delete(Long permissionId);
}
