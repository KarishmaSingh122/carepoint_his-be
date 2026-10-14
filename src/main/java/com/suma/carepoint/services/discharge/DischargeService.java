package com.suma.carepoint.services.discharge;


import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.descharge.CreateDischargeRequest;

public interface DischargeService {

    ApiResponse createDischarge(CreateDischargeRequest request);

    ApiResponse getDischarge(Long dischargeId);

    ApiResponse getAllDischarges();

    ApiResponse updateDischarge(
            Long dischargeId,
            CreateDischargeRequest request);

    ApiResponse deleteDischarge(Long dischargeId);
}
