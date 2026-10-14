package com.suma.carepoint.controllers;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.bed.CreateBedRequest;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.services.bed.BedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstant.Controller.HMIS)
public class BedController {
    @Autowired
    private BedService bedService;

    @PostMapping(ApiConstant.Bed.CREATE)
    public ResponseEntity<ApiResponse> createBed(@RequestBody CreateBedRequest createBedRequest) {
        return ResponseEntity.ok(bedService.createBed(createBedRequest));
    }

    @GetMapping(ApiConstant.Bed.GET_BY_ID)
    public ResponseEntity<ApiResponse> getBed(@PathVariable Long bedId) {
        return ResponseEntity.ok(bedService.getBed(bedId));
    }

    @GetMapping(ApiConstant.Bed.GET_ALL)
    public ResponseEntity<ApiResponse> getAllBeds() {
        return ResponseEntity.ok(bedService.getAllBeds());
    }

    @GetMapping(ApiConstant.Bed.GET_ALL_BY_ROOM_ID+"/{roomId}")
    public ResponseEntity<ApiResponse> getAllBedsByRoomId(@PathVariable Long roomId) {
        return ResponseEntity.ok(bedService.getAllBedsByRoomId(roomId));
    }

    @GetMapping(ApiConstant.Bed.GET_AVAILABLE_BEDS_BY_ROOM_ID+"/{roomId}")
    public ResponseEntity<ApiResponse> getAllAvailableBedsByRoomId(@PathVariable Long roomId) {
        return ResponseEntity.ok(bedService.getAllAvailableBedsByRoomId(roomId));
    }

    @PutMapping(ApiConstant.Bed.UPDATE)
    public ResponseEntity<ApiResponse> updateBed(@PathVariable Long bedId, @RequestBody CreateBedRequest createBedRequest) {
        return ResponseEntity.ok(bedService.updateBed(bedId, createBedRequest));
    }

    @DeleteMapping(ApiConstant.Bed.DELETE)
    public ResponseEntity<ApiResponse> deleteBed(@PathVariable Long bedId) {
        return ResponseEntity.ok(bedService.deleteBed(bedId)
        );
    }
}

