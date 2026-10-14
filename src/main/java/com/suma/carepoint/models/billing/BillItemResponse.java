package com.suma.carepoint.models.billing;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillItemResponse {

    private Long billItemId;

    private Long serviceId;

    private String serviceName;

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal discount;

    private BigDecimal amount;
}