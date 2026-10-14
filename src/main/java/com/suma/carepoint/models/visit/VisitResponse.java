package com.suma.carepoint.models.visit;


import com.suma.carepoint.entities.visit.VisitStatus;
import com.suma.carepoint.entities.visit.VisitType;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitResponse {

    private Long visitId;

    private Long patientId;

    private Long doctorId;
    private String doctorEmployeeNo;
    private String doctorFirstName;
    private String doctorLastName;

    private Long departmentId;
    private String departmentCode;
    private String departmentName;

    private VisitType visitType;
    private Instant visitDate;
    private String reason;
    private VisitStatus status;


    private Long diagnosisId;
    private String diagnosisCode;
    private String diagnosisName;

    private Instant createdAt;
    private Instant updatedAt;

}
