package com.suma.carepoint.controllers;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.models.utility.PageResponse;
import com.suma.carepoint.models.visit.VisitRequest;
import com.suma.carepoint.models.visit.VisitResponse;
import com.suma.carepoint.services.visit.VisitService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstant.Visit.BASE)
@RequiredArgsConstructor
@Validated
public class VisitController {

    private final VisitService visitService;

    @PostMapping(ApiConstant.Visit.CREATE)
    public ResponseEntity<ApiResponse> create(
            @Valid @RequestBody VisitRequest request
    ) {

        VisitResponse response =
                visitService.create(request);

        ApiResponse apiResponse =
                new ApiResponse(
                        1,
                        "",
                        response
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiResponse);
    }

    @GetMapping(ApiConstant.Visit.GET_BY_ID)
    public ResponseEntity<ApiResponse> getById(
            @PathVariable
            @Min(1)
            Long visitId
    ) {

        VisitResponse response =
                visitService.getById(visitId);

        ApiResponse apiResponse =
                new ApiResponse(
                        1,
                        "",
                        response
                );

        return ResponseEntity.ok()
                .body(apiResponse);
    }

    @GetMapping(ApiConstant.Visit.GET_ALL)
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam(required = false)
            @Min(1)
            Long patientId,

            @RequestParam(required = false)
            @Min(1)
            Long doctorId,

            @RequestParam(required = false)
            @Min(1)
            Long departmentId,

            @RequestParam(required = false)
            String status,

            @RequestParam(defaultValue = "0")
            @Min(0)
            int page,

            @RequestParam(defaultValue = "20")
            @Min(1)
            @Max(100)
            int size
    ) {

        PageResponse response =
                visitService.getAll(
                        patientId,
                        doctorId,
                        departmentId,
                        status,
                        page,
                        size
                );

        ApiResponse apiResponse =
                new ApiResponse(
                        1,
                        "",
                        response
                );

        return ResponseEntity.ok()
                .body(apiResponse);
    }

    @PutMapping(ApiConstant.Visit.UPDATE)
    public ResponseEntity<ApiResponse> update(
            @PathVariable
            @Min(1)
            Long visitId,

            @Valid @RequestBody VisitRequest request
    ) {

        VisitResponse response =
                visitService.update(
                        visitId,
                        request
                );

        ApiResponse apiResponse =
                new ApiResponse(
                        1,
                        "",
                        response
                );

        return ResponseEntity.ok()
                .body(apiResponse);
    }

    @PatchMapping(ApiConstant.Visit.STATUS)
    public ResponseEntity<ApiResponse> updateStatus(
            @PathVariable
            @Min(1)
            Long visitId,
            @RequestParam String status
    ) {

        VisitResponse response =
                visitService.updateStatus(
                        visitId,
                        status
                );

        ApiResponse apiResponse =
                new ApiResponse(
                        1,
                        "",
                        response
                );

        return ResponseEntity.ok()
                .body(apiResponse);
    }
}
