package com.suma.carepoint.repositories.emr;

import com.suma.carepoint.entities.emr.Diagnosis;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis,Long> {
    boolean existsByDiagnosisCodeIgnoreCase(@NotBlank(message = "Diagnosis code is required") @Size(max = 30, message = "Diagnosis code must not exceed 30 characters") String diagnosisCode);

    boolean existsByDiagnosisNameIgnoreCase(@NotBlank(message = "Diagnosis name is required") @Size(max = 200, message = "Diagnosis name must not exceed 200 characters") String diagnosisName);

    boolean existsByDiagnosisNameIgnoreCaseAndDiagnosisIdNot(@NotBlank(message = "Diagnosis name is required") @Size(max = 200, message = "Diagnosis name must not exceed 200 characters") String diagnosisName, Long diagnosisId);

    boolean existsByDiagnosisCodeIgnoreCaseAndDiagnosisIdNot(@NotBlank(message = "Diagnosis code is required") @Size(max = 30, message = "Diagnosis code must not exceed 30 characters") String diagnosisCode, Long diagnosisId);

    List<Diagnosis> searchActiveDiagnoses(String searchKeyword);
}
