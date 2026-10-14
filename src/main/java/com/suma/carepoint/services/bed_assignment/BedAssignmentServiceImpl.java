package com.suma.carepoint.services.bed_assignment;

import com.suma.carepoint.entities.admission.Admission;
import com.suma.carepoint.entities.bed.Bed;
import com.suma.carepoint.entities.bedassignment.BedAssignment;
import com.suma.carepoint.entities.patient.Patient;
import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.bed_assignemnt.BedAssignmentResponse;
import com.suma.carepoint.models.bed_assignemnt.CreateBedAssignmentRequest;
import com.suma.carepoint.repositories.admisssion.AdmissionRepository;
import com.suma.carepoint.repositories.bed.BedRepository;
import com.suma.carepoint.repositories.bed_assignment.BedAssignmentRepository;
import com.suma.carepoint.repositories.patient.PatientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class BedAssignmentServiceImpl implements BedAssignmentService {
    private final BedAssignmentRepository bedAssignmentRepository;
    private final BedRepository bedRepository;
    private final PatientRepository patientRepository;
    private final AdmissionRepository admissionRepository;
    private final ModelMapper modelMapper;

    public BedAssignmentServiceImpl(BedAssignmentRepository bedAssignmentRepository,
            BedRepository bedRepository,
            PatientRepository patientRepository,
            AdmissionRepository admissionRepository,
            ModelMapper modelMapper) {

        this.bedAssignmentRepository = bedAssignmentRepository;
        this.bedRepository = bedRepository;
        this.patientRepository = patientRepository;
        this.admissionRepository = admissionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApiResponse createAssignment(
            CreateBedAssignmentRequest request) {

        Bed bed = bedRepository.findById(request.getBedId())
                .orElseThrow(() -> new RuntimeException("Bed not found"));

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Admission admission = admissionRepository.findById(request.getAdmissionId())
                .orElseThrow(() -> new RuntimeException("Admission not found"));

        BedAssignment assignment =
                modelMapper.map(request, BedAssignment.class);

        assignment.setBed(bed);
        assignment.setPatient(patient);
        assignment.setAdmission(admission);

        if (request.getAssignedAt() == null) {
            assignment.setAssignedAt(OffsetDateTime.now());
        }

        if (request.getActive() == null) {
            assignment.setActive(true);
        }
        assignment.setCreatedAt(OffsetDateTime.now());

        BedAssignment savedAssignment = bedAssignmentRepository.save(assignment);

        BedAssignmentResponse response = mapToResponse(savedAssignment);

        return new ApiResponse(1, "Bed assigned successfully", response);
    }

    @Override
    public ApiResponse getAssignment(Long assignmentId) {

        BedAssignment assignment = bedAssignmentRepository.findById(assignmentId)
                        .orElseThrow(() -> new RuntimeException("Bed assignment not found"));

        return new ApiResponse(1, "Bed assignment fetched successfully", mapToResponse(assignment));
    }

    @Override
    public ApiResponse getAllAssignments() {

        List<BedAssignment> assignments = bedAssignmentRepository.findAll();

        List<BedAssignmentResponse> responses = assignments.stream()
                .map(this::mapToResponse).toList();

        return new ApiResponse(1, "Bed assignments fetched successfully", responses);
    }

    @Override
    public ApiResponse updateAssignment(
            Long assignmentId,
            CreateBedAssignmentRequest request) {

        BedAssignment existingAssignment = bedAssignmentRepository.findById(assignmentId)
                        .orElseThrow(() -> new RuntimeException("Bed assignment not found"));

        Bed bed = bedRepository.findById(request.getBedId())
                .orElseThrow(() -> new RuntimeException("Bed not found"));

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Admission admission = admissionRepository.findById(request.getAdmissionId())
                        .orElseThrow(() -> new RuntimeException("Admission not found"));

        modelMapper.map(request, existingAssignment);

        existingAssignment.setBed(bed);
        existingAssignment.setPatient(patient);
        existingAssignment.setAdmission(admission);

        BedAssignment updatedAssignment = bedAssignmentRepository.save(existingAssignment);

        return new ApiResponse(1, "Bed assignment updated successfully", mapToResponse(updatedAssignment));
    }

    @Override
    public ApiResponse releaseAssignment(Long assignmentId) {

        BedAssignment assignment = bedAssignmentRepository.findById(assignmentId).orElseThrow(() -> new RuntimeException("Bed assignment not found"));

        assignment.setActive(false);
        assignment.setReleasedAt(OffsetDateTime.now());

        BedAssignment releasedAssignment = bedAssignmentRepository.save(assignment);

        return new ApiResponse(1, "Bed assignment released successfully", mapToResponse(releasedAssignment));
    }

    @Override
    public ApiResponse deleteAssignment(Long assignmentId) {

        BedAssignment assignment = bedAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Bed assignment not found"));

        bedAssignmentRepository.delete(assignment);

        return new ApiResponse(1, "Bed assignment deleted successfully", null);
    }

    private BedAssignmentResponse mapToResponse(
            BedAssignment assignment) {

        BedAssignmentResponse response = modelMapper.map(assignment, BedAssignmentResponse.class);

        response.setBedId(assignment.getBed().getBedId());

        response.setPatientId(assignment.getPatient().getPatientId()
        );

        response.setAdmissionId(assignment.getAdmission().getAdmissionId());

        return response;
    }
}


