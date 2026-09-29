package com.suma.carepoint.controllers;

import com.suma.carepoint.entities.wardrooms.WardRoom;
import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.models.wardrooms.CreateRoomRequest;
import com.suma.carepoint.services.rooms.WardRoomService;
import com.suma.carepoint.services.rooms.WardRoomServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RequestMapping(ApiConstant.Controller.HMIS)
@RestController
public class WardRoomController {

    @Autowired
    private WardRoomServiceImpl wardRoomService;

    @PostMapping(ApiConstant.WardRoom.CREATE)
    public ResponseEntity<ApiResponse> createRoom(@RequestBody CreateRoomRequest createRoomRequest) {
        return ResponseEntity.ok(wardRoomService.createRoom(createRoomRequest));
    }

    @GetMapping(ApiConstant.WardRoom.GET_BY_ID)
    public ResponseEntity<ApiResponse> getRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(wardRoomService.getRoom(roomId));
    }

    @GetMapping(ApiConstant.WardRoom.GET_ALL)
    public ResponseEntity<ApiResponse> getAllRooms() {
        return ResponseEntity.ok(wardRoomService.getAllRooms());
    }

    @PutMapping(ApiConstant.WardRoom.UPDATE)
    public ResponseEntity<ApiResponse> updateRoom(@RequestBody WardRoom wardRoom) {
        return ResponseEntity.ok(wardRoomService.updateRoom(wardRoom));
    }

    @DeleteMapping(ApiConstant.WardRoom.DELETE)
    public ResponseEntity<ApiResponse> deleteRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(wardRoomService.deleteRooom(roomId)
        );
    }
}


