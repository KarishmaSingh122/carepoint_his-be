package com.suma.carepoint.services.organization;


import com.suma.carepoint.models.organization.StaffRequest;
import com.suma.carepoint.models.organization.StaffResponse;
import com.suma.carepoint.models.utility.PageResponse;

public interface StaffService {

    StaffResponse createStaff(StaffRequest request);

    StaffResponse getStaffById(Long staffId);

    PageResponse getStaff(String search, Boolean active, Long departmentId, int page, int size);

    PageResponse getActiveStaff(int page, int size);

    PageResponse getStaffByDepartment(Long departmentId, int page, int size);

    PageResponse getStaffByDesignation(String designation, int page, int size);

    StaffResponse updateStaff(Long staffId, StaffRequest request);

    StaffResponse updateStaffStatus(Long staffId, boolean staus);

    void deleteStaff(Long staffId);
}
