package com.suma.carepoint.repositories.organization;

import com.suma.carepoint.entities.organization.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository
        extends JpaRepository<Department, Long> {

    Optional<Department> findByDepartmentCodeIgnoreCase(
            String departmentCode
    );

    Optional<Department> findByDepartmentNameIgnoreCase(
            String departmentName
    );

    boolean existsByDepartmentCodeIgnoreCase(
            String departmentCode
    );

    boolean existsByDepartmentNameIgnoreCase(
            String departmentName
    );

    boolean existsByDepartmentCodeIgnoreCaseAndDepartmentIdNot(
            String departmentCode,
            Long departmentId
    );

    boolean existsByDepartmentNameIgnoreCaseAndDepartmentIdNot(
            String departmentName,
            Long departmentId
    );

    List<Department> findByActiveTrueOrderByDepartmentNameAsc();

    @Query("""
    SELECT d
    FROM Department d
    WHERE
        (:active IS NULL OR d.active = :active)
    AND
        (
            CAST(:search AS string) IS NULL
            OR LOWER(d.departmentCode) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
            OR LOWER(d.departmentName) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
        )
    """)
    Page<Department> searchDepartments(
            @Param("search") String search,
            @Param("active") Boolean active,
            Pageable pageable
    );
}
