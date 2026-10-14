package com.suma.carepoint.services.bed_assignment;

import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.bed_assignemnt.CreateBedAssignmentRequest;

public interface BedAssignmentService {

    ApiResponse createAssignment(CreateBedAssignmentRequest request);

    ApiResponse getAssignment(Long assignmentId);

    ApiResponse getAllAssignments();

    ApiResponse updateAssignment(Long assignmentId, CreateBedAssignmentRequest request);

    ApiResponse releaseAssignment(Long assignmentId);

    ApiResponse deleteAssignment(Long assignmentId);
}

