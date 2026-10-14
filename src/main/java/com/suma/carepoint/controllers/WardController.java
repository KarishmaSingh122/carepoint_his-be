package com.suma.carepoint.controllers;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.models.ward.CreateWardRequest;
import com.suma.carepoint.services.ward.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(ApiConstant.Controller.HMIS)
@RestController
public class WardController {

    @Autowired
    private WardService wardService;

    @PostMapping(ApiConstant.Ward.CREATE)
    public ResponseEntity<ApiResponse> createWard(@RequestBody CreateWardRequest request) {
        return ResponseEntity.ok().body(wardService.createWard(request));
    }

    @GetMapping(ApiConstant.Ward.GET_ALL)
    public ResponseEntity<ApiResponse> getAllWards() {
        return ResponseEntity.ok().body(wardService.getAllWards());
    }

    @GetMapping(ApiConstant.Ward.GET_BY_ID)
    public ResponseEntity<ApiResponse> getWardById(@RequestParam Long wardId) {
        return ResponseEntity.ok().body(wardService.getWardById(wardId));
    }

    @PutMapping(ApiConstant.Ward.UPDATE)
    public ResponseEntity<ApiResponse> updateWard(@RequestParam Long wardId, @RequestBody CreateWardRequest request) {
        return ResponseEntity.ok().body( wardService.updateWard(wardId, request));
    }

    @DeleteMapping(ApiConstant.Ward.DELETE)
    public ResponseEntity<ApiResponse> deleteWard(@RequestParam Long wardId) {
        return ResponseEntity.ok().body(wardService.deleteWard(wardId));
    }


}