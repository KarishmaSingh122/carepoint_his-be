package com.suma.carepoint.models.billing;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBillItemRequest {

    private Long serviceId;

    private Integer quantity;
}