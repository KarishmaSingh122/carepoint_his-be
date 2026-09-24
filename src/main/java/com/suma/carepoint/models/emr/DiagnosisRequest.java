package com.suma.carepoint.models.emr;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiagnosisRequest {

    @NotBlank(message = "Diagnosis code is required")
    @Size(max = 30, message = "Diagnosis code must not exceed 30 characters")
    private String diagnosisCode;

    @NotBlank(message = "Diagnosis name is required")
    @Size(max = 200, message = "Diagnosis name must not exceed 200 characters")
    private String diagnosisName;

    private String description;
}