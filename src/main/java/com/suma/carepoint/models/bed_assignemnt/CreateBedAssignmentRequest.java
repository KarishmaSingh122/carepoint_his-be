package com.suma.carepoint.models.bed_assignemnt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateBedAssignmentRequest {

    private Long bedId;

    private Long patientId;

    private Long admissionId;

    private OffsetDateTime assignedAt;

    private Boolean active;
}

