package com.suma.carepoint.services.discharge;

import com.suma.carepoint.entities.admission.Admission;
import com.suma.carepoint.entities.discharge.Discharge;
import com.suma.carepoint.entities.organization.Staff;
import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.descharge.CreateDischargeRequest;
import com.suma.carepoint.models.descharge.DischargeResponse;
import com.suma.carepoint.repositories.admisssion.AdmissionRepository;
import com.suma.carepoint.repositories.discharge.DischargeRepository;
import com.suma.carepoint.repositories.organization.StaffRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class DischargeServiceImpl implements DischargeService {
    private final DischargeRepository dischargeRepository;
    private final AdmissionRepository admissionRepository;
    private final StaffRepository staffRepository;
    private final ModelMapper modelMapper;

    public DischargeServiceImpl(
            DischargeRepository dischargeRepository,
            AdmissionRepository admissionRepository,
            StaffRepository staffRepository,
            ModelMapper modelMapper) {

        this.dischargeRepository = dischargeRepository;
        this.admissionRepository = admissionRepository;
        this.staffRepository = staffRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApiResponse createDischarge(
            CreateDischargeRequest request) {

        Admission admission = admissionRepository.findById(request.getAdmissionId())
                        .orElseThrow(() -> new RuntimeException("Admission not found"));

        Staff doctor = null;

        if (request.getDoctorId() != null) {
            doctor = staffRepository.findById(request.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));
        }

        Discharge discharge = modelMapper.map(request, Discharge.class);

        discharge.setAdmission(admission);
        discharge.setDoctor(doctor);

        if (request.getDischargeDate() == null) {
            discharge.setDischargeDate(OffsetDateTime.now());
        }

        discharge.setCreatedAt(OffsetDateTime.now());

        Discharge savedDischarge = dischargeRepository.save(discharge);

        return new ApiResponse(1, "Discharge created successfully", mapToResponse(savedDischarge));
    }

    @Override
    public ApiResponse getDischarge(Long dischargeId) {

        Discharge discharge = dischargeRepository.findById(dischargeId)
                .orElseThrow(() -> new RuntimeException("Discharge not found"));

        return new ApiResponse(1, "Discharge fetched successfully", mapToResponse(discharge));
    }

    @Override
    public ApiResponse getAllDischarges() {

        List<Discharge> discharges = dischargeRepository.findAll();

        List<DischargeResponse> responses = discharges.stream().map(this::mapToResponse)
                        .toList();

        return new ApiResponse(1, "Discharges fetched successfully", responses);
    }

    @Override
    public ApiResponse updateDischarge(Long dischargeId, CreateDischargeRequest request) {

        Discharge existingDischarge = dischargeRepository.findById(dischargeId)
                        .orElseThrow(() -> new RuntimeException("Discharge not found"));

        Admission admission = admissionRepository.findById(request.getAdmissionId())
                        .orElseThrow(() -> new RuntimeException("Admission not found"));

        Staff doctor = null;

        if (request.getDoctorId() != null) {
            doctor = staffRepository.findById(request.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));
        }

        modelMapper.map(request, existingDischarge);

        existingDischarge.setAdmission(admission);
        existingDischarge.setDoctor(doctor);

        Discharge updatedDischarge = dischargeRepository.save(existingDischarge);

        return new ApiResponse(1, "Discharge updated successfully", mapToResponse(updatedDischarge));
    }

    @Override
    public ApiResponse deleteDischarge(Long dischargeId) {

        Discharge discharge = dischargeRepository.findById(dischargeId)
                .orElseThrow(() -> new RuntimeException("Discharge not found"));

        dischargeRepository.delete(discharge);

        return new ApiResponse(1, "Discharge deleted successfully", null);
    }

    private DischargeResponse mapToResponse(Discharge discharge) {

        DischargeResponse response = modelMapper.map(discharge, DischargeResponse.class);

        response.setAdmissionId(discharge.getAdmission().getAdmissionId());

        if (discharge.getDoctor() != null) {
            response.setDoctorId(discharge.getDoctor().getStaffId());
        }

        return response;
    }
}

