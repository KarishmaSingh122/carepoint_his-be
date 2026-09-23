package com.suma.carepoint.models.admission;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAdmissionRequest {

    private String admissionNumber;

    private Long patientId;

    private OffsetDateTime admissionDate;

    private AdmissionType admissionType;

    private AdmissionStatus status;

    private String reason;

    private Long admittingDoctorId;

}
