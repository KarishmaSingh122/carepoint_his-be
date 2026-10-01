package com.suma.carepoint.services.rooms;

import com.suma.carepoint.entities.wardrooms.WardRoom;
import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.wardrooms.CreateRoomRequest;
import org.jspecify.annotations.Nullable;

public interface WardRoomService {

    ApiResponse createRoom(CreateRoomRequest createRoomRequest);

    ApiResponse getRoom(Long roomId );

    ApiResponse getAllRooms();

    ApiResponse deleteRooom(Long roomId);

    ApiResponse updateRoom(WardRoom wardRoom);

    ApiResponse getRoomByWardId(Long wardId);
}
