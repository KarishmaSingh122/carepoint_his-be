package com.suma.carepoint.models.billing;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateBillRequest {

    private BigDecimal discount;

    private BigDecimal tax;

    private List<CreateBillItemRequest> items;
}
