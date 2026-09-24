package com.suma.carepoint.models.emr;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiagnosisResponse {

    private Long diagnosisId;

    private String diagnosisCode;

    private String diagnosisName;

    private String description;

    private Boolean active;

    private Instant createdAt;

    private Instant updatedAt;
}