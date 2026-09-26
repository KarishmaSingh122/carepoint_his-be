package com.suma.carepoint.repositories.medical_record;

import com.suma.carepoint.entities.medical_record.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalRecordRepository
        extends JpaRepository<MedicalRecord, Long> {

    Page<MedicalRecord> findByPatientPatientId(
            Long patientId,
            Pageable pageable
    );

    Page<MedicalRecord> findByVisitVisitId(
            Long visitId,
            Pageable pageable
    );

    Page<MedicalRecord> findByDoctorStaffId(
            Long doctorId,
            Pageable pageable
    );

    Page<MedicalRecord> findByDiagnosisDiagnosisId(
            Long diagnosisId,
            Pageable pageable
    );

    @Query("""
            SELECT mr
            FROM MedicalRecord mr
            WHERE (:patientId IS NULL OR mr.patient.patientId = :patientId)
              AND (:visitId IS NULL OR mr.visit.visitId = :visitId)
              AND (:doctorId IS NULL OR mr.doctor.staffId = :doctorId)
              AND (:diagnosisId IS NULL OR mr.diagnosis.diagnosisId = :diagnosisId)
            """)
    Page<MedicalRecord> search(
            @Param("patientId")
            Long patientId,

            @Param("visitId")
            Long visitId,

            @Param("doctorId")
            Long doctorId,

            @Param("diagnosisId")
            Long diagnosisId,

            Pageable pageable
    );
}
