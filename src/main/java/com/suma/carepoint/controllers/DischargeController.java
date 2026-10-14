package com.suma.carepoint.controllers;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.models.descharge.CreateDischargeRequest;
import com.suma.carepoint.services.discharge.DischargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(ApiConstant.Controller.HMIS)
public class DischargeController {

    @Autowired
    private DischargeService dischargeService;

    @PostMapping(ApiConstant.Discharge.CREATE)
    public ResponseEntity<ApiResponse> createDischarge(
            @RequestBody CreateDischargeRequest request) {

        return ResponseEntity.ok(
                dischargeService.createDischarge(request)
        );
    }

    @GetMapping(ApiConstant.Discharge.GET_BY_ID)
    public ResponseEntity<ApiResponse> getDischarge(
            @PathVariable Long dischargeId) {

        return ResponseEntity.ok(
                dischargeService.getDischarge(dischargeId)
        );
    }

    @GetMapping(ApiConstant.Discharge.GET_ALL)
    public ResponseEntity<ApiResponse> getAllDischarges() {

        return ResponseEntity.ok(
                dischargeService.getAllDischarges()
        );
    }

    @PutMapping(ApiConstant.Discharge.UPDATE)
    public ResponseEntity<ApiResponse> updateDischarge(
            @PathVariable Long dischargeId,
            @RequestBody CreateDischargeRequest request) {

        return ResponseEntity.ok(
                dischargeService.updateDischarge(
                        dischargeId,
                        request
                )
        );
    }

    @DeleteMapping(ApiConstant.Discharge.DELETE)
    public ResponseEntity<ApiResponse> deleteDischarge(
            @PathVariable Long dischargeId) {

        return ResponseEntity.ok(
                dischargeService.deleteDischarge(dischargeId)
        );
    }
}

