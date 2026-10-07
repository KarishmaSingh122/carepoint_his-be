package com.suma.carepoint.repositories.admisssion;

import com.suma.carepoint.entities.admission.Admission;
import com.suma.carepoint.entities.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AdmissionRepository extends JpaRepository<Admission,Long>{
    Optional<Admission> findByAdmissionNumber(String admissionNumber);

    @Query("SELECT a.patient FROM Admission a WHERE a.status = 'ADMITTED'")
    List<Patient> findAdmittedPatients();

    Optional<Admission> findByPatientPatientId(Long pateintId);

    @Query("""
    SELECT a
    FROM Admission a
    JOIN FETCH a.patient p
    WHERE a.status = 'ADMITTED'
      AND (
            CAST(a.admissionNumber AS string) LIKE CONCAT('%', :keyword, '%')
            OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(CONCAT(p.firstName, ' ', p.lastName))
                LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(CONCAT(p.lastName, ' ', p.firstName))
                LIKE LOWER(CONCAT('%', :keyword, '%'))
          )
    ORDER BY a.admissionId DESC
""")
    List<Admission> searchActiveAdmissionsByKeyword(
            @Param("keyword") String keyword
    );
}
