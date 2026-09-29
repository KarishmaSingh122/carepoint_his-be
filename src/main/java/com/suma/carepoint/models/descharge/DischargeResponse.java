package com.suma.carepoint.models.descharge;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DischargeResponse {

    private Long dischargeId;

    private Long admissionId;

    private Long doctorId;

    private OffsetDateTime dischargeDate;

    private String dischargeType;

    private String dischargeSummary;

    private OffsetDateTime createdAt;
}
