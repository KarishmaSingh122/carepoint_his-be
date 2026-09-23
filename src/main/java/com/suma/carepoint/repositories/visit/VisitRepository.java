package com.suma.carepoint.repositories.visit;


import com.suma.carepoint.entities.visit.Visit;
import com.suma.carepoint.entities.visit.VisitStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    Page<Visit> findByPatientPatientId(
            Long patientId,
            Pageable pageable
    );

    Page<Visit> findByDoctorStaffId(
            Long doctorId,
            Pageable pageable
    );

    Page<Visit> findByDepartmentDepartmentId(
            Long departmentId,
            Pageable pageable
    );

    Page<Visit> findByStatus(
            VisitStatus status,
            Pageable pageable
    );

    Page<Visit> findByPatientPatientIdAndStatus(
            Long patientId,
            VisitStatus status,
            Pageable pageable
    );

    Page<Visit> findByDoctorStaffIdAndStatus(
            Long doctorId,
            VisitStatus status,
            Pageable pageable
    );

    Page<Visit> findByDepartmentDepartmentIdAndStatus(
            Long departmentId,
            VisitStatus status,
            Pageable pageable
    );
}
