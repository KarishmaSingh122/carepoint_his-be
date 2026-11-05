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

    Optional<Patient> findByAbhaNumber(String abhaNumber);

    Page<Patient> findByActive(Boolean active, Pageable pageable);

    @Query("""
        SELECT p
        FROM Patient p
        WHERE
            CAST(p.patientId AS string) LIKE CONCAT('%', :keyword, '%')
            OR LOWER(p.abhaNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(p.patientMob) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(p.patientName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        ORDER BY p.patientName ASC
    """)
    List<Patient> searchByKeyword(@Param("keyword") String keyword);

    boolean existsByAbhaNumber(String abhaId);

    boolean existsByAbhaNumberAndActiveTrue(String abhaId);

}
