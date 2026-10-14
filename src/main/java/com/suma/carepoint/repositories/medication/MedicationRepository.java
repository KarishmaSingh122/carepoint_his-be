package com.suma.carepoint.repositories.medication;

import com.suma.carepoint.entities.medication.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicationRepository extends JpaRepository<Medication,Long> {

    List<Medication> findByActiveTrue();

    List<Medication> findByNameContainingIgnoreCaseAndActiveTrue(
            String name
    );

    List<Medication> findByGenericNameContainingIgnoreCaseAndActiveTrue(
            String genericName
    );

    Optional<Medication> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndMedicationIdNot(String medicationName, Long medicationId);
}
