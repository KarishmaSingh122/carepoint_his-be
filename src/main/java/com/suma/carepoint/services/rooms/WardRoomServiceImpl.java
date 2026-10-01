package com.suma.carepoint.services.rooms;
import com.suma.carepoint.entities.ward.Ward;
import com.suma.carepoint.entities.wardrooms.WardRoom;
import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.wardrooms.CreateRoomRequest;
import com.suma.carepoint.models.wardrooms.RoomResponse;
import com.suma.carepoint.repositories.ward.WardRepository;
import com.suma.carepoint.repositories.wardroom.WardRoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WardRoomServiceImpl implements WardRoomService {

    private final WardRoomRepository wardRoomRepository;
    private final WardRepository wardRepository;
    private final ModelMapper modelMapper;

    public WardRoomServiceImpl(
            WardRoomRepository wardRoomRepository,
            WardRepository wardRepository,
            ModelMapper modelMapper) {

        this.wardRoomRepository = wardRoomRepository;
        this.wardRepository = wardRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public ApiResponse createRoom(CreateRoomRequest createRoomRequest) {

        Ward ward = wardRepository.findById(createRoomRequest.getWardId())
                .orElseThrow(() -> new RuntimeException("Ward not found"));

        WardRoom wardRoom = modelMapper.map(createRoomRequest, WardRoom.class);

        wardRoom.setWard(ward);

        WardRoom savedWardRoom = wardRoomRepository.save(wardRoom);

        RoomResponse roomResponse = modelMapper.map(savedWardRoom, RoomResponse.class);

        roomResponse.setWardId(ward.getWardId());

        return new ApiResponse(1, "Room created successfully", roomResponse);
    }

    @Override
    public ApiResponse getRoom(Long roomId) {

        WardRoom wardRoom = wardRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        RoomResponse roomResponse = modelMapper.map(wardRoom, RoomResponse.class);

        roomResponse.setWardId(wardRoom.getWard().getWardId());

        return new ApiResponse(1, "Room fetched successfully", roomResponse);
    }

    @Override
    public ApiResponse getAllRooms() {

        List<WardRoom> rooms = wardRoomRepository.findAll();

        List<RoomResponse> roomResponses = rooms.stream()
                .map(room -> {
                    RoomResponse response = modelMapper.map(room, RoomResponse.class);

                    response.setWardId(room.getWard().getWardId());

                    return response;}).toList();

        return new ApiResponse(1, "Rooms fetched successfully", roomResponses);
    }

    @Override
    public ApiResponse deleteRooom(Long roomId) {

        WardRoom wardRoom = wardRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));

        wardRoomRepository.delete(wardRoom);

        return new ApiResponse(1, "Room deleted successfully", null);}

    @Override
    public ApiResponse updateRoom(WardRoom wardRoom) {

        WardRoom existingRoom = wardRoomRepository.findById(wardRoom.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        modelMapper.map(wardRoom, existingRoom);

        WardRoom updatedRoom = wardRoomRepository.save(existingRoom);

        RoomResponse roomResponse = modelMapper.map(updatedRoom, RoomResponse.class);

        roomResponse.setWardId(updatedRoom.getWard().getWardId());

        return new ApiResponse(1, "Room updated successfully", roomResponse);
    }

    @Override
    public ApiResponse getRoomByWardId(Long wardId) {
        // 1. Fetch the list of rooms or throw an exception if none are found
        List<WardRoom> wardRoomList = wardRoomRepository.findAllByWardWardId(wardId);
//                .orElseThrow(() -> new RuntimeException("Rooms not found for ward ID: " + wardId));

        // 2. Map the list of WardRoom entities to a list of RoomResponse DTOs
        List<RoomResponse> roomResponses = wardRoomList.stream()
                .map(wardRoom -> {
                    RoomResponse response = modelMapper.map(wardRoom, RoomResponse.class);
                    response.setWardId(wardRoom.getWard().getWardId());
                    return response;
                }).toList();

        // 3. Return the list inside your ApiResponse
        return new ApiResponse(1, "Rooms fetched successfully", roomResponses);
    }
}