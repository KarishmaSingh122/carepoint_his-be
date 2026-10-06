package com.suma.carepoint.services.ward;

import com.suma.carepoint.entities.ward.Ward;
import com.suma.carepoint.entities.wardrooms.WardRoom;
import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.ward.CreateWardRequest;
import com.suma.carepoint.models.wardrooms.CreateRoomRequest;
import org.jspecify.annotations.Nullable;

public interface WardService {

    ApiResponse createWard(CreateWardRequest request);

    ApiResponse getWardById(Long wardId);

    ApiResponse getAllWards();

    ApiResponse updateWard(Long wardId, CreateWardRequest request);

    ApiResponse deleteWard(Long wardId);
}


