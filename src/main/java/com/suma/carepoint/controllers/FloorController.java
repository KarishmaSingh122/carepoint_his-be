package com.suma.carepoint.controllers;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.models.floor.CreateFloorRequest;
import com.suma.carepoint.services.floor.FloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstant.Controller.HMIS)
public class FloorController {

    @Autowired
    private FloorService floorService;

    @PostMapping(ApiConstant.Floor.CREATE)
    public ApiResponse createFloor(@RequestBody CreateFloorRequest request) {
        return floorService.createFloor(request);
    }

    @GetMapping(ApiConstant.Floor.GET_ALL)
    public ApiResponse getAllFloors() {
        return floorService.getAllFloors();
    }

    @GetMapping(ApiConstant.Floor.GET_BY_ID)
    public ApiResponse getFloorById(@RequestParam Long floorId) {
        return floorService.getFloorById(floorId);
    }

    @PutMapping(ApiConstant.Floor.UPDATE)
    public ApiResponse updateFloor(@RequestParam Long floorId, @RequestBody CreateFloorRequest request) {
        return floorService.updateFloor(floorId, request);
    }

    @DeleteMapping(ApiConstant.Floor.DELETE)
    public ApiResponse deleteFloor(@RequestParam Long floorId) {
        return floorService.deleteFloor(floorId);
    }}