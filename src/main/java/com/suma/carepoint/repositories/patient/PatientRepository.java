package com.suma.carepoint.repositories.patient;

import com.suma.carepoint.entities.patient.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository  extends JpaRepository<Patient,Long> {
    Optional<Patient> findByAbhaId(String abhaId);

    @Query("""
        SELECT p
        FROM Patient p
        WHERE
            CAST(p.patientId AS string) LIKE CONCAT('%', :keyword, '%')
            OR LOWER(p.abhaId) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(p.phone) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        ORDER BY p.firstName ASC, p.lastName ASC
    """)
    List<Patient> searchByKeyword(@Param("keyword") String keyword);

    Page<Patient> findByActive(boolean active, Pageable pageable);
}
