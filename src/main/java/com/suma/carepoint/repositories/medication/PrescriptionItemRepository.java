package com.suma.carepoint.repositories.medication;

import com.suma.carepoint.entities.medication.PrescriptionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionItemRepository extends JpaRepository<PrescriptionItem,Long> {

    List<PrescriptionItem> findByPrescriptionPrescriptionId(Long prescriptionId);

    List<PrescriptionItem> findByMedicationMedicationId(Long medicationId);

    boolean existsByMedicationMedicationId(Long medicationId);

}
