
package com.suma.carepoint.entities.patient;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientPolicyDetails {

    private String insurancePolicyType;
    private String insuranceCompanyName;
    private String policyNumber;
    private LocalDate policyEndDate;
    private String memberId;
    private LocalDate policyStartDate;
    private String insuranceProviderNumber;
    private LocalDate insuranceBenefitStartDate;
    private LocalDate insuranceBenefitEndDate;
    private String recipientCode;
}