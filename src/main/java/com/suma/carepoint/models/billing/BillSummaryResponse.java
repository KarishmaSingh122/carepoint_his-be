package com.suma.carepoint.models.billing;

import com.suma.carepoint.entities.bill.EBillStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillSummaryResponse {

    private Long billId;

    private String billNo;

    private Long patientId;

    private Long visitId;

    private Long admissionId;

    private OffsetDateTime billDate;

    private BigDecimal totalAmount;

    private BigDecimal paidAmount;

    private BigDecimal balanceAmount;

    private EBillStatus status;
}