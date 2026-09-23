package com.suma.carepoint.repositories.admisssion;

import com.suma.carepoint.entities.admission.Admission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdmissionRepository extends JpaRepository<Admission,Long>{
    Optional<Admission> findByAdmissionNumber(String admissionNumber);

}
