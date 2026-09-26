package com.suma.carepoint.models.medical_record;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalRecordRequest {

    @NotNull
    @Positive
    private Long patientId;

    @Positive
    private Long visitId;

    @Positive
    private Long doctorId;

    @Positive
    private Long diagnosisId;

    @PastOrPresent
    private Instant recordDate;

    @Size(max = 10000)
    private String symptoms;

    @Size(max = 10000)
    private String treatment;

    @Size(max = 10000)
    private String notes;
}
