package com.suma.carepoint.services.organization;

import com.suma.carepoint.entities.organization.Department;
import com.suma.carepoint.entities.organization.Staff;
import com.suma.carepoint.exceptions.ConflictException;
import com.suma.carepoint.exceptions.ResourceNotFoundException;
import com.suma.carepoint.models.auth.RoleResponse;
import com.suma.carepoint.models.auth.UserRequest;
import com.suma.carepoint.models.auth.UserResponse;
import com.suma.carepoint.models.auth.UserRoleResponse;
import com.suma.carepoint.models.mapper.StaffMapper;
import com.suma.carepoint.models.organization.StaffRequest;
import com.suma.carepoint.models.organization.StaffResponse;
import com.suma.carepoint.models.utility.PageResponse;
import com.suma.carepoint.repositories.auth.RoleRepository;
import com.suma.carepoint.repositories.onehcx.OneHcxAccountRepository;
import com.suma.carepoint.repositories.organization.DepartmentRepository;
import com.suma.carepoint.repositories.organization.StaffRepository;
import com.suma.carepoint.security.CurrentUserService;
import com.suma.carepoint.services.auth.RoleService;
import com.suma.carepoint.services.auth.UserRoleService;
import com.suma.carepoint.services.auth.UserService;
import com.suma.carepoint.services.onehcx.auth.OneHcxAuthService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.suma.carepoint.models.utility.PageResponse.buildPageResponse;
import static com.suma.carepoint.models.utility.PageResponse.validatePagination;
import static com.suma.carepoint.models.utility.TextNormalizationUtils.normalize;
import static com.suma.carepoint.models.utility.TextNormalizationUtils.normalizeSearch;
@Service
@Slf4j
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final DepartmentRepository departmentRepository;
    private final StaffMapper staffMapper;
    private final UserService userService;
    private final CurrentUserService currentUserService;
    private final RoleService roleService;
    private final UserRoleService userRoleService;
    private final JdbcTemplate jdbcTemplate;
    @Value("${app.default-password}")
    private String defaultPassword;


    @Override
    public StaffResponse createStaff(StaffRequest request) {
        String employeeNo = generateEmployeeNo();
        String email = normalizeEmail(request.getEmail());
        validateDuplicateStaff(employeeNo, email, null);
        Department department = findDepartment(request.getDepartmentId());
        RoleResponse role = roleService.getById(request.getRoleId());
        if(currentUserService.hasRole(role.getRoleName())){
            throw new AccessDeniedException("You are not authorized to assign this role: " + role.getRoleName());
        }
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

            UserRequest userRequest =  UserRequest.builder().staffId(savedStaff.getStaffId()).username(savedStaff.getEmail()).password(defaultPassword).roleId(role.getRoleId()).build();
            userService.create(userRequest);
            log.debug("Staff created successfully.");
            return staffMapper.toResponse(savedStaff);
        } catch (DataIntegrityViolationException exception) {
            log.error("Failed to create staff because of database constraint. employeeNo={}", employeeNo);
            throw new ConflictException("Employee number or email already exists");
        }
    }

    @Override
    public StaffResponse getStaffById(Long staffId) {
        Staff staff = findStaffById(staffId);
        return mapStaffWithRoles(staff);
    }

    @Override
    public PageResponse getStaff(String search, Boolean active, Long departmentId, int page, int size) {
        validatePagination(page, size);
        if (departmentId != null) {
            findDepartment(departmentId);
        }

        boolean isSuperAdmin = currentUserService.hasRole("SUPER_ADMIN");
        boolean isHospitalAdmin = currentUserService.hasRole("HOSPITAL_ADMIN");

        String normalizedSearch = normalizeSearch(search);
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.ASC, "lastName")
                        .and(Sort.by(Sort.Direction.ASC, "firstName")));
        Page<Staff> staffPage = staffRepository.searchStaff(normalizedSearch, active, departmentId,
                currentUserService.getActiveContext().staffId(), isSuperAdmin, isHospitalAdmin, pageable);

        return buildPageResponse(staffPage, mapStaffWithRoles(staffPage.getContent()));
    }

    @Override
    public PageResponse getActiveStaff(int page, int size) {
        validatePagination(page, size);

        boolean isSuperAdmin = currentUserService.hasRole("SUPER_ADMIN");

        Page<Staff> staffPage = staffRepository.findVisibleActiveStaff(
                currentUserService.getActiveContext().staffId(),
                isSuperAdmin,
                PageRequest.of(
                        page,
                        size,
                        Sort.by(Sort.Direction.ASC, "lastName")
                                .and(Sort.by(Sort.Direction.ASC, "firstName"))
                )
        );

        return buildPageResponse(staffPage, mapStaffWithRoles(staffPage.getContent()));
    }

    @Override
    public PageResponse getStaffByDepartment(Long departmentId, int page, int size) {
        validatePagination(page, size);
        findDepartment(departmentId);

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.ASC, "lastName")
                        .and(Sort.by(Sort.Direction.ASC, "firstName")));

        Page<Staff> staffPage = staffRepository.findByDepartmentDepartmentId(departmentId, pageable);
        return buildPageResponse(staffPage, mapStaffWithRoles(staffPage.getContent()));
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

        return buildPageResponse(staffPage, mapStaffWithRoles(staffPage.getContent()));
    }

    @Override
    public StaffResponse updateStaff(Long staffId, StaffRequest request) {
        Staff staff = findStaffById(staffId);
        String employeeNo = normalize(staff.getEmployeeNo());
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
            return mapStaffWithRoles(updatedStaff);
        } catch (DataIntegrityViolationException exception) {
            log.error("Failed to update staff because of database constraint. staffId={}", staffId);
            throw new ConflictException("Employee number or email already exists");
        }
    }

    @Override
    public StaffResponse updateStaffStatus(Long staffId, boolean status) {
        Staff staff = findStaffById(staffId);
        staff.setActive(status);
        userService.updateStatusByStaffId(staff.getStaffId(),status);
        Staff updatedStaff = staffRepository.save(staff);
        log.debug("Staff status updated.");
        return mapStaffWithRoles(updatedStaff);
    }

    @Override
    public void deleteStaff(Long staffId) {
        Staff staff = findStaffById(staffId);
        staffRepository.delete(staff);
        log.info("Staff deleted successfully.");
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

    private String normalizeEmail(String email) {
        String normalized = normalize(email);
        if (normalized == null) {
            return null;
        }
        return normalized.toLowerCase();
    }

    private String generateEmployeeNo() {
        Long sequence = jdbcTemplate.queryForObject(
                "SELECT nextval('employee_number_sequence')",
                Long.class
        );

        return "EMP" + String.format("%06d", sequence);
    }

    private StaffResponse mapStaffWithRoles(Staff staff) {
        return mapStaffWithRoles(List.of(staff)).get(0);
    }

    private List<StaffResponse> mapStaffWithRoles(List<Staff> staffList) {

        if (staffList == null || staffList.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> staffIds = staffList.stream()
                .map(Staff::getStaffId)
                .toList();

        Map<Long, String> roleMap =
                userRoleService.getRoleNamesByStaffIds(staffIds);

        return staffList.stream()
                .map(staff -> {
                    StaffResponse response = staffMapper.toResponse(staff);
                    response.setRoleName(roleMap.get(staff.getStaffId()));
                    return response;
                })
                .toList();
    }
}
