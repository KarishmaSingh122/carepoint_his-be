package com.suma.carepoint.services.bed;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.bed.CreateBedRequest;

public interface BedService {
    ApiResponse createBed(CreateBedRequest createBedRequest);

    ApiResponse getBed(Long bedId);

    ApiResponse getAllBeds();

    ApiResponse updateBed(Long bedId, CreateBedRequest createBedRequest);

    ApiResponse deleteBed(Long bedId);
}
