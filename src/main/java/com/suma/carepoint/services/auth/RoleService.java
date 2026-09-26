package com.suma.carepoint.services.auth;


import com.suma.carepoint.models.auth.RoleRequest;
import com.suma.carepoint.models.auth.RoleResponse;
import com.suma.carepoint.models.utility.PageResponse;

public interface RoleService {

    RoleResponse create(RoleRequest request);

    RoleResponse getById(Long roleId);

    PageResponse getAll(String search, Boolean active, int page, int size);

    RoleResponse update(Long roleId, RoleRequest request);

    RoleResponse updateStatus(Long roleId, boolean active);

    void delete(Long roleId);
}
