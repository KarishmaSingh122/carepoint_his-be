package com.suma.carepoint.repositories.organization;

import com.suma.carepoint.entities.organization.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    Optional<Staff> findByEmployeeNoIgnoreCase(String employeeNo);

    Optional<Staff> findByEmailIgnoreCase(String email);

    boolean existsByEmployeeNoIgnoreCase(String employeeNo);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmployeeNoIgnoreCaseAndStaffIdNot(String employeeNo, Long staffId);

    boolean existsByEmailIgnoreCaseAndStaffIdNot(String email, Long staffId);

    Page<Staff> findByDepartmentDepartmentId(Long departmentId, Pageable pageable);

    Page<Staff> findByDesignationIgnoreCase(String designation, Pageable pageable);

    Page<Staff> findByActiveTrue(Pageable pageable);

    @Query("""
            SELECT s
            FROM Staff s
            JOIN s.department d
            WHERE
                (
                    CAST(:search AS string) IS NULL
                    OR LOWER(s.employeeNo)
                        LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
                    OR LOWER(s.firstName)
                        LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
                    OR LOWER(s.lastName)
                        LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
                    OR LOWER(CONCAT(s.firstName, ' ', s.lastName))
                        LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
                    OR LOWER(s.email)
                        LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
                    OR LOWER(s.designation)
                        LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
                    OR LOWER(s.specialization)
                        LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
                    OR LOWER(d.departmentName)
                        LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
                    OR LOWER(d.departmentCode)
                        LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
                )
            AND
                (
                    :active IS NULL
                    OR s.active = :active
                )
            AND
                (
                    :departmentId IS NULL
                    OR d.departmentId = :departmentId
                )
            """)
    Page<Staff> searchStaff(@Param("search") String search, @Param("active") Boolean active, @Param("departmentId") Long departmentId, Pageable pageable);
}
