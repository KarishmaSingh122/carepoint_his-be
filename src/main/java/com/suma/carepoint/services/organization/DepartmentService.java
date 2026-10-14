package com.suma.carepoint.services.organization;


import com.suma.carepoint.models.organization.DepartmentRequest;
import com.suma.carepoint.models.organization.DepartmentResponse;
import com.suma.carepoint.models.utility.PageResponse;

public interface DepartmentService {

    DepartmentResponse createDepartment(DepartmentRequest request);

    DepartmentResponse getDepartmentById(Long departmentId);

    PageResponse getDepartments(
            String search, Boolean active,
            int page, int size);

    PageResponse getActiveDepartments(int page, int size);

    DepartmentResponse updateDepartment(Long departmentId, DepartmentRequest request);

    DepartmentResponse updateDepartmentStatus(Long departmentId, boolean status);

    void deleteDepartment(Long departmentId);
}
