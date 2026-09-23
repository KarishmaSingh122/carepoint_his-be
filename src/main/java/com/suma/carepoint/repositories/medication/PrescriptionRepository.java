package com.suma.carepoint.repositories.medication;

import com.suma.carepoint.entities.medication.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription,Long> {
    List<Prescription> findByPatientPatientId(Long patientId);
//
//    List<Prescription> findByVisitVisitId(Long visitId);
//
//    List<Prescription> findByDoctorStaffId(Long doctorId);

    List<Prescription> findByPatientPatientIdAndActiveTrue(Long patientId);

    List<Prescription> findByPatientPatientIdOrderByPrescriptionDateDesc(Long patientId);

//    List<Prescription> findByVisitVisitIdOrderByPrescriptionDateDesc(Long visitId);
//
//    List<Prescription> findByDoctorStaffIdOrderByPrescriptionDateDesc(Long doctorId);

//    List<Prescription> findByDoctorStaffIdOrderByPrescriptionDateDesc(Long doctorId);
}
