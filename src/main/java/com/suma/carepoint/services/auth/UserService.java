package com.suma.carepoint.services.auth;


import com.suma.carepoint.models.auth.UserRequest;
import com.suma.carepoint.models.auth.UserResponse;
import com.suma.carepoint.models.utility.PageResponse;

public interface UserService {

    UserResponse create(UserRequest request);

    UserResponse getById(Long userId);

    PageResponse getAll(String search, Boolean active, int page, int size);

    UserResponse getByStaffId(Long staffId);

    UserResponse update(Long userId, UserRequest request);

    UserResponse updateStatus(Long userId, boolean active);

    void delete(Long userId);
}
