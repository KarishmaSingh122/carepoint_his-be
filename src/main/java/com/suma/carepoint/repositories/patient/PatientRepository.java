package com.suma.carepoint.repositories.patient;

import com.suma.carepoint.entities.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository  extends JpaRepository<Patient,Long> {
    Optional<Patient> findByAbhaId(String abhaId);
}
