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
public class BedAssignmentResponse {
    private Long assignmentId;

    private Long bedId;

    private Long patientId;

    private Long admissionId;

    private OffsetDateTime assignedAt;

    private OffsetDateTime releasedAt;

    private Boolean active;

    private OffsetDateTime createdAt;
}
