package com.suma.carepoint.models.treatment_workspace;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentWorkspaceTreatmentResponse {

    private Long treatmentId;

    private Instant treatmentDate;

    private Long doctorId;

    private String doctorName;

    private Long procedureId;

    private String procedureName;

    private String description;

    private String remarks;

    private String status;
}
