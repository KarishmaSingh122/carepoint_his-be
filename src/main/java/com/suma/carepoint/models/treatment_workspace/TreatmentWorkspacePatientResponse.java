package com.suma.carepoint.models.treatment_workspace;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentWorkspacePatientResponse {

    private Long patientId;

    private String patientName;

    private String gender;

    private LocalDate dateOfBirth;

    private String mobileNo;
}
