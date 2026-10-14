package com.suma.carepoint.repositories.procedure;

import com.suma.carepoint.entities.procedure.PatientTreatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientTreatmentRepository extends JpaRepository<PatientTreatment,Long> {
    List<PatientTreatment>
    findByAdmissionAdmissionIdOrderByTreatmentDateDesc(
            Long admissionId
    );

    List<PatientTreatment>
    findByDoctorStaffIdOrderByTreatmentDateDesc(
            Long doctorId
    );

    List<PatientTreatment>
    findByProcedureProcedureIdOrderByTreatmentDateDesc(
            Long procedureId
    );

    boolean existsByProcedureProcedureId(Long procedureId);
}
