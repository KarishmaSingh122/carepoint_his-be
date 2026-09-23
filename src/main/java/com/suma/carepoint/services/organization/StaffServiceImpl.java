package com.suma.carepoint.services.organization;

import com.suma.carepoint.entities.organization.Department;
import com.suma.carepoint.entities.organization.Staff;
import com.suma.carepoint.exceptions.ConflictException;
import com.suma.carepoint.exceptions.ResourceNotFoundException;
import com.suma.carepoint.models.mapper.StaffMapper;
import com.suma.carepoint.models.organization.StaffRequest;
import com.suma.carepoint.models.organization.StaffResponse;
import com.suma.carepoint.models.utility.PageResponse;
import com.suma.carepoint.repositories.organization.DepartmentRepository;
import com.suma.carepoint.repositories.organization.StaffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.suma.carepoint.models.utility.PageResponse.buildPageResponse;
import static com.suma.carepoint.models.utility.PageResponse.validatePagination;

@Service
@RequiredArgsConstructor
@Slf4j
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;

    private final DepartmentRepository departmentRepository;

    private final StaffMapper staffMapper;

    @Override
    public StaffResponse createStaff(StaffRequest request) {
        String employeeNo = normalize(request.getEmployeeNo());
        String email = normalizeEmail(request.getEmail());
        validateDuplicateStaff(employeeNo, email, null);
        Department department = findDepartment(request.getDepartmentId());
        Staff staff = staffMapper.toEntity(request);

        staff.setDepartment(department);
        staff.setEmployeeNo(employeeNo);
        staff.setEmail(email);
        staff.setFirstName(normalize(request.getFirstName()));
        staff.setLastName(normalize(request.getLastName()));
        staff.setPhone(normalize(request.getPhone()));
        staff.setDesignation(normalize(request.getDesignation()));
        staff.setSpecialization(normalize(request.getSpecialization()));
        staff.setActive(true);
        try {
            Staff savedStaff = staffRepository.save(staff);
            log.info("Staff created successfully. staffId={}, employeeNo={}, departmentId={}",
                    savedStaff.getStaffId(), savedStaff.getEmployeeNo(), department.getDepartmentId());
            return staffMapper.toResponse(savedStaff);
        } catch (DataIntegrityViolationException exception) {
            log.error("Failed to create staff because of database constraint. employeeNo={}", employeeNo);
            throw new ConflictException("Employee number or email already exists");
        }
    }

    @Override
    public StaffResponse getStaffById(Long staffId) {
        Staff staff = findStaffById(staffId);
        return staffMapper.toResponse(staff);
    }

    @Override
    public PageResponse getStaff(String search, Boolean active, Long departmentId, int page, int size) {
        validatePagination(page, size);
        if (departmentId != null) {
            findDepartment(departmentId);
        }
        String normalizedSearch = normalizeSearch(search);

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.ASC, "lastName")
                        .and(Sort.by(Sort.Direction.ASC, "firstName")));
        Page<Staff> staffPage = staffRepository.searchStaff(normalizedSearch, active, departmentId, pageable);

        return buildPageResponse(
                staffPage,
                staffPage.getContent().stream().map(staffMapper::toResponse).toList());
    }

    @Override
    public PageResponse getActiveStaff(int page, int size) {
        validatePagination(page, size);
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.ASC, "lastName")
                        .and(Sort.by(Sort.Direction.ASC, "firstName")));
        Page<Staff> staffPage = staffRepository.findByActiveTrue(pageable);

        return buildPageResponse(
                staffPage,
                staffPage.getContent().stream().map(staffMapper::toResponse).toList());
    }

    @Override
    public PageResponse getStaffByDepartment(Long departmentId, int page, int size) {
        validatePagination(page, size);
        findDepartment(departmentId);

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.ASC, "lastName")
                        .and(Sort.by(Sort.Direction.ASC, "firstName")));

        Page<Staff> staffPage = staffRepository.findByDepartmentDepartmentId(departmentId, pageable);
        return buildPageResponse(
                staffPage,
                staffPage.getContent().stream().map(staffMapper::toResponse).toList());
    }

    @Override
    public PageResponse getStaffByDesignation(String designation, int page, int size) {
        validatePagination(page, size);
        if (!StringUtils.hasText(designation)) {
            throw new IllegalArgumentException("Designation is required");
        }

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.ASC, "lastName")
                .and(Sort.by(Sort.Direction.ASC, "firstName")));
        Page<Staff> staffPage = staffRepository.findByDesignationIgnoreCase(designation.trim(), pageable);

        return buildPageResponse(
                staffPage,
                staffPage.getContent().stream().map(staffMapper::toResponse).toList());
    }

    @Override
    public StaffResponse updateStaff(Long staffId, StaffRequest request) {
        Staff staff = findStaffById(staffId);
        String employeeNo = normalize(request.getEmployeeNo());
        String email = normalizeEmail(request.getEmail());
        validateDuplicateStaff(employeeNo, email, staffId);
        Department department = findDepartment(request.getDepartmentId());
        staffMapper.updateEntity(request, staff);

        staff.setDepartment(department);
        staff.setEmployeeNo(employeeNo);
        staff.setEmail(email);
        staff.setFirstName(normalize(request.getFirstName()));
        staff.setLastName(normalize(request.getLastName()));
        staff.setPhone(normalize(request.getPhone()));
        staff.setDesignation(normalize(request.getDesignation()));
        staff.setSpecialization(normalize(request.getSpecialization()));
        try {
            Staff updatedStaff = staffRepository.save(staff);
            log.info("Staff updated successfully. staffId={}, departmentId={}", staffId, department.getDepartmentId());
            return staffMapper.toResponse(updatedStaff);
        } catch (DataIntegrityViolationException exception) {
            log.error("Failed to update staff because of database constraint. staffId={}", staffId);
            throw new ConflictException("Employee number or email already exists");
        }
    }

    @Override
    public StaffResponse updateStaffStatus(Long staffId, boolean status) {
        Staff staff = findStaffById(staffId);
        boolean oldStatus = staff.isActive();
        staff.setActive(status);
        Staff updatedStaff = staffRepository.save(staff);
        log.info("Staff status updated. staffId={}, oldStatus={}, newStatus={}", staffId, oldStatus, status);
        return staffMapper.toResponse(updatedStaff);
    }

    @Override
    public void deleteStaff(Long staffId) {
        Staff staff = findStaffById(staffId);
        staffRepository.delete(staff);
        log.info("Staff deleted successfully. staffId={}", staffId);
    }

    private Staff findStaffById(Long staffId) {
        return staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + staffId));
    }

    private Department findDepartment(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + departmentId));
    }

    private void validateDuplicateStaff(String employeeNo, String email, Long staffId) {
        boolean employeeNoExists;
        boolean emailExists = false;
        if (staffId == null) {
            employeeNoExists = staffRepository.existsByEmployeeNoIgnoreCase(employeeNo);
            if (StringUtils.hasText(email)) {
                emailExists = staffRepository.existsByEmailIgnoreCase(email);
            }

        } else {
            employeeNoExists = staffRepository.existsByEmployeeNoIgnoreCaseAndStaffIdNot(employeeNo, staffId);
            if (StringUtils.hasText(email)) {
                emailExists = staffRepository.existsByEmailIgnoreCaseAndStaffIdNot(email, staffId);
            }
        }
        if (employeeNoExists) {
            throw new ConflictException("Employee number already exists");
        }
        if (emailExists) {
            throw new ConflictException("Email already exists");
        }
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }

    private String normalizeEmail(String email) {
        String normalized = normalize(email);
        if (normalized == null) {
            return null;
        }
        return normalized.toLowerCase();
    }

    private String normalizeSearch(String search) {
        if (!StringUtils.hasText(search)) {
            return null;
        }
        return search.trim();
    }
}
