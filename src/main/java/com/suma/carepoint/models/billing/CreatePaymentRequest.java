package com.suma.carepoint.models.billing;

import com.suma.carepoint.entities.bill.EPaymentMethod;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePaymentRequest {

    private BigDecimal amount;

    private EPaymentMethod paymentMethod;

    private String transactionReference;
}