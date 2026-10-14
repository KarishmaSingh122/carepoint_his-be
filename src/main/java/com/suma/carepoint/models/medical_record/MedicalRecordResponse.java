package com.suma.carepoint.models.medical_record;


import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalRecordResponse {

    private Long medicalRecordId;

    private Long patientId;
    private Long visitId;
    private Long doctorId;
    private Long diagnosisId;

    private Instant recordDate;
    private String symptoms;
    private String treatment;
    private String notes;

    private Instant createdAt;
    private Instant updatedAt;
}
