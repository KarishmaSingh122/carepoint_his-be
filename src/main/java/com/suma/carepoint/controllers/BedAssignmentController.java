package com.suma.carepoint.controllers;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.bed_assignemnt.CreateBedAssignmentRequest;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.services.bed_assignment.BedAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstant.Controller.HMIS)
public class BedAssignmentController {

        @Autowired
        private BedAssignmentService bedAssignmentService;

        @PostMapping(ApiConstant.BedAssignment.CREATE)
        public ResponseEntity<ApiResponse> createAssignment(@RequestBody CreateBedAssignmentRequest request) {
            return ResponseEntity.ok(bedAssignmentService.createAssignment(request));
        }

        @GetMapping(ApiConstant.BedAssignment.GET_BY_ID)
        public ResponseEntity<ApiResponse> getAssignment(@PathVariable Long assignmentId) {
            return ResponseEntity.ok(bedAssignmentService.getAssignment(assignmentId));
        }

        @GetMapping(ApiConstant.BedAssignment.GET_ALL)
        public ResponseEntity<ApiResponse> getAllAssignments() {
            return ResponseEntity.ok(bedAssignmentService.getAllAssignments());
        }

        @PutMapping(ApiConstant.BedAssignment.UPDATE)
        public ResponseEntity<ApiResponse> updateAssignment(@PathVariable Long assignmentId, @RequestBody CreateBedAssignmentRequest request) {
            return ResponseEntity.ok(bedAssignmentService.updateAssignment(assignmentId, request));
        }

        @PutMapping(ApiConstant.BedAssignment.RELEASE)
        public ResponseEntity<ApiResponse> releaseAssignment(@PathVariable Long assignmentId) {
            return ResponseEntity.ok(bedAssignmentService.releaseAssignment(assignmentId));
        }

        @DeleteMapping(ApiConstant.BedAssignment.DELETE)
        public ResponseEntity<ApiResponse> deleteAssignment(@PathVariable Long assignmentId) {
            return ResponseEntity.ok(bedAssignmentService.deleteAssignment(assignmentId));
        }
    }

