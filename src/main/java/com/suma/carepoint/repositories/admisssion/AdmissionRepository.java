package com.suma.carepoint.repositories.admisssion;

import com.suma.carepoint.entities.admission.Admission;
import com.suma.carepoint.entities.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AdmissionRepository extends JpaRepository<Admission,Long>{
    Optional<Admission> findByAdmissionNumber(String admissionNumber);

    @Query("SELECT a.patient FROM Admission a WHERE a.status = 'ADMITTED'")
    List<Patient> findAdmittedPatients();

    Optional<Admission> findByPatientPatientId(Long pateintId);
}
