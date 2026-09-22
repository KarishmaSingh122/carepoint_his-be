package com.suma.carepoint.models.medical_service;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateServiceRequest {

    private String serviceCode;

    private String serviceName;

    private Long serviceCategoryId;

    private BigDecimal defaultPrice;
}
