package com.suma.carepoint.models.procedure;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientTreatmentResponse {

    private Long treatmentId;

    private Long admissionId;

    private Long doctorId;
    private String doctorName;

    private Long procedureId;
    private String procedureCode;
    private String procedureName;

    private Instant treatmentDate;

    private String description;
    private String remarks;

    private Instant createdAt;
    private Instant updatedAt;
}