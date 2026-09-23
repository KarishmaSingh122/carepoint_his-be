package com.suma.carepoint.models.admission;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdmissionResponse {

    private Long admissionId;

    private String admissionNumber;

    private Long patientId;

    private OffsetDateTime admissionDate;

    private AdmissionType admissionType;

    private AdmissionStatus status;

    private String reason;

    private Long admittingDoctorId;
}
