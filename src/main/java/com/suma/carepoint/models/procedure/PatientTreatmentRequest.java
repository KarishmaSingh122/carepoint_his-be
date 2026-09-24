package com.suma.carepoint.models.procedure;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientTreatmentRequest {

    @NotNull
    private Long admissionId;

    private Long doctorId;

    private Long procedureId;

    private Instant treatmentDate;

    private String description;

    private String remarks;
}
