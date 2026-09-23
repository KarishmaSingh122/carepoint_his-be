package com.suma.carepoint.services.floor;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.floor.CreateFloorRequest;

public interface FloorService {

    ApiResponse createFloor(CreateFloorRequest request);

    ApiResponse getAllFloors();

    ApiResponse getFloorById(Long floorId);

    ApiResponse updateFloor(Long floorId, CreateFloorRequest request);

    ApiResponse deleteFloor(Long floorId);
}
