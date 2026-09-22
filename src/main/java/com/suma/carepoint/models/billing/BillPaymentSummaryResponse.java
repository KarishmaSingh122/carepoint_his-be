package com.suma.carepoint.models.billing;

import com.suma.carepoint.entities.bill.EPaymentMethod;
import com.suma.carepoint.entities.bill.EPaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillPaymentSummaryResponse {

    private Long paymentId;

    private Long billId;

    private String billNo;

    private BigDecimal amount;

    private EPaymentMethod paymentMethod;

    private String transactionReference;

    private EPaymentStatus paymentStatus;

    private OffsetDateTime paymentDate;
}