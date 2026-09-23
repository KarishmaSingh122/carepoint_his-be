package com.suma.carepoint.services.organization;

import com.suma.carepoint.entities.organization.Department;
import com.suma.carepoint.exceptions.ConflictException;
import com.suma.carepoint.exceptions.ResourceNotFoundException;
import com.suma.carepoint.models.mapper.DepartmentMapper;
import com.suma.carepoint.models.organization.DepartmentRequest;
import com.suma.carepoint.models.organization.DepartmentResponse;
import com.suma.carepoint.models.utility.PageResponse;
import com.suma.carepoint.repositories.organization.DepartmentRepository;
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
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        String departmentCode = normalize(request.getDepartmentCode());
        String departmentName = normalize(request.getDepartmentName());
        validateDuplicateDepartment(departmentCode, departmentName, null);
        Department department = departmentMapper.toEntity(request);
        department.setDepartmentCode(departmentCode);
        department.setDepartmentName(departmentName);
        department.setActive(true);
        try {
            Department savedDepartment = departmentRepository.save(department);
            log.info("Department created successfully. departmentId={}, departmentCode={}",
                    savedDepartment.getDepartmentId(), savedDepartment.getDepartmentCode());
            return departmentMapper.toResponse(savedDepartment);
        } catch (DataIntegrityViolationException exception) {
            log.error("Failed to create department due to data integrity constraint. departmentCode={}", departmentCode);
            throw new ConflictException("Department code or department name already exists");
        }
    }

    @Override
    public DepartmentResponse getDepartmentById(Long departmentId) {
        Department department = findDepartmentById(departmentId);
        return departmentMapper.toResponse(department);
    }

    @Override
    public PageResponse getDepartments(String search, Boolean active, int page, int size) {
        validatePagination(page, size);
        String normalizedSearch = normalizeSearch(search);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "departmentName"));
        Page<Department> departmentPage = departmentRepository.searchDepartments(normalizedSearch, active, pageable);
        return buildPageResponse(
                departmentPage,
                departmentPage.getContent().stream().map(departmentMapper::toResponse).toList());
    }

    @Override
    public PageResponse getActiveDepartments(int page, int size) {
        validatePagination(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "departmentName"));
        Page<Department> departmentPage = departmentRepository.searchDepartments(null, true, pageable);
        return buildPageResponse(
                departmentPage,
                departmentPage.getContent().stream().map(departmentMapper::toResponse).toList());
    }

    @Override
    public DepartmentResponse updateDepartment(Long departmentId, DepartmentRequest request) {
        Department department = findDepartmentById(departmentId);
        String departmentCode = normalize(request.getDepartmentCode());
        String departmentName = normalize(request.getDepartmentName());

        validateDuplicateDepartment(departmentCode, departmentName, departmentId);
        departmentMapper.updateEntity(request, department);
        department.setDepartmentCode(departmentCode);
        department.setDepartmentName(departmentName);
        try {
            Department updatedDepartment = departmentRepository.save(department);
            log.info("Department updated successfully. departmentId={}", departmentId);
            return departmentMapper.toResponse(updatedDepartment);
        } catch (DataIntegrityViolationException exception) {
            log.error("Failed to update department due to data integrity constraint. departmentId={}", departmentId);
            throw new ConflictException("Department code or department name already exists");
        }
    }

    @Override
    public DepartmentResponse updateDepartmentStatus(Long departmentId, boolean status) {
        Department department = findDepartmentById(departmentId);
        boolean oldStatus = department.isActive();
        department.setActive(status);
        Department updatedDepartment = departmentRepository.save(department);

        log.info("Department status updated. departmentId={}, oldStatus={}, newStatus={}", departmentId, oldStatus, status);
        return departmentMapper.toResponse(updatedDepartment);
    }

    @Override
    public void deleteDepartment(Long departmentId) {
        Department department = findDepartmentById(departmentId);
        departmentRepository.delete(department);
        log.info("Department deleted successfully. departmentId={}", departmentId);
    }

    private Department findDepartmentById(Long departmentId) {
        return departmentRepository.findById(
                departmentId).orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + departmentId)
        );
    }

    private void validateDuplicateDepartment(String departmentCode, String departmentName, Long departmentId) {
        boolean codeExists;
        boolean nameExists;
        if (departmentId == null) {
            codeExists = departmentRepository.existsByDepartmentCodeIgnoreCase(departmentCode);
            nameExists = departmentRepository.existsByDepartmentNameIgnoreCase(departmentName);
        } else {
            codeExists = departmentRepository.existsByDepartmentCodeIgnoreCaseAndDepartmentIdNot(departmentCode, departmentId);
            nameExists = departmentRepository.existsByDepartmentNameIgnoreCaseAndDepartmentIdNot(departmentName, departmentId);
        }
        if (codeExists) {
            throw new ConflictException("Department code already exists");
        }
        if (nameExists) {
            throw new ConflictException("Department name already exists");
        }
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        return value.trim();
    }

    private String normalizeSearch(String search) {
        if (!StringUtils.hasText(search)) {
            return null;
        }
        return search.trim();
    }

}
