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
public class TreatmentWorkspaceAdmissionResponse {

    private Long admissionId;

    private OffsetDateTime admissionDate;

    private String admissionType;

    private Long departmentId;

    private String departmentName;

    private Long wardId;

    private String wardName;

    private Long roomId;

    private String roomNumber;

    private Long bedId;

    private String bedNumber;

    private Long admittingDoctorId;

    private String admittingDoctorName;

    private String status;
}
