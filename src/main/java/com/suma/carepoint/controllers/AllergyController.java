package com.suma.carepoint.controllers;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.allergy.CreateAllergyRequest;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.services.allergy.AllergyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstant.Controller.HMIS)
public class AllergyController {

    @Autowired
    private AllergyService allergyService;

    @PostMapping(ApiConstant.Allergy.CREATE)
    public ResponseEntity<ApiResponse> createAllergy(@RequestBody CreateAllergyRequest request) {
        return ResponseEntity.ok(allergyService.createAllergy(request));
    }

    @GetMapping(ApiConstant.Allergy.GET_BY_ID)
    public ResponseEntity<ApiResponse> getAllergy(@PathVariable Long allergyId) {
        return ResponseEntity.ok(allergyService.getAllergy(allergyId));
    }

    @GetMapping(ApiConstant.Allergy.GET_ALL)
    public ResponseEntity<ApiResponse> getAllAllergies() {
        return ResponseEntity.ok(allergyService.getAllAllergies());
    }

    @PutMapping(ApiConstant.Allergy.UPDATE)
    public ResponseEntity<ApiResponse> updateAllergy(@PathVariable Long allergyId, @RequestBody CreateAllergyRequest request) {
        return ResponseEntity.ok(allergyService.updateAllergy(allergyId, request)

        );
    }

    @DeleteMapping(ApiConstant.Allergy.DELETE)
    public ResponseEntity<ApiResponse> deleteAllergy(@PathVariable Long allergyId) {
        return ResponseEntity.ok(allergyService.deleteAllergy(allergyId)

        );
    }
}
