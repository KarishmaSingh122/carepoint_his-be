package com.suma.carepoint.services.patient;

import com.suma.carepoint.entities.patient.Patient;
import com.suma.carepoint.exceptions.BadRequestException;
import com.suma.carepoint.exceptions.ConflictException;
import com.suma.carepoint.exceptions.ResourceNotFoundException;
import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.patient.CreatePatientRequest;
import com.suma.carepoint.models.patient.PatientResponse;
import com.suma.carepoint.models.utility.PageResponse;
import com.suma.carepoint.repositories.patient.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.suma.carepoint.models.utility.PageResponse.buildPageResponse;
import static com.suma.carepoint.models.utility.PageResponse.validatePagination;
@Service
@RequiredArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse getPatientByPatientId(Long patientId) {

        Patient patient = findPatient(patientId);
        PatientResponse response = modelMapper.map(patient, PatientResponse.class);
        return new ApiResponse(1, "Patients found successfully", response);
    }

    @Override
    public ApiResponse getPatientByAbhaId(String abhaId) {

        Patient patient = patientRepository.findByAbhaNumber(abhaId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        PatientResponse response = modelMapper.map(patient, PatientResponse.class);
        return new ApiResponse(1, "Patients found successfully", response);
    }

    @Override
    public ApiResponse createPatients(CreatePatientRequest request, Map<String, MultipartFile> files) {

        validateRequest(request);
        if (patientRepository.findByAbhaNumber(request.getAbhaNumber()).isPresent()) {
            throw new ConflictException("Patient with ABHA number already exists");
        }

        Patient patient = modelMapper.map(request, Patient.class);
        patient.setAge(calculateAgeFromDob(patient));
        applyAddressRule(patient);
        Patient savedPatient = patientRepository.save(patient);
        PatientResponse response = modelMapper.map(savedPatient, PatientResponse.class);

        return new ApiResponse(1, "Patients created successfully", response);
    }

    private static @NonNull String calculateAgeFromDob(Patient patient) {
        LocalDate dob = patient.getPatientDob();
        Period agePeriod = Period.between(dob, LocalDate.now());
        String age = agePeriod.getYears() + " years "
                + agePeriod.getMonths() + " months";
        return age;
    }

    @Override
    public ApiResponse updatePatient(Long patientId, CreatePatientRequest request) {

//        validateRequest(request);
        Patient patient = findPatient(patientId);

        patientRepository.findByAbhaNumber(request.getAbhaNumber())
                .filter(existing -> !existing.getPatientId().equals(patientId))
                .ifPresent(existing -> {
                    throw new ConflictException("Patient with ABHA number already exists");
                });

        modelMapper.map(request, patient);
        applyAddressRule(patient);
        Patient updatedPatient = patientRepository.save(patient);
        PatientResponse response = modelMapper.map(updatedPatient, PatientResponse.class);

        return new ApiResponse(1, "Patients updated successfully", response);
    }

    @Override
    public ApiResponse DeletePatientByPatientId(Long patientId) {

        Patient patient = findPatient(patientId);
        patient.setActive(false);
        Patient updatedPatient = patientRepository.save(patient);
        PatientResponse response = modelMapper.map(updatedPatient, PatientResponse.class);

        return new ApiResponse(1, "Patients deleted successfully", response);
    }

    @Override
    public PageResponse getAllPatients(Boolean active, int page, int size) {

        validatePagination(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Patient> patients;

        if (active != null) {
            patients = patientRepository.findByActive(active, pageable);
        } else {
            patients = patientRepository.findAll(pageable);
        }

        return buildPageResponse(
                patients,
                patients.getContent()
                        .stream()
                        .map(patient -> modelMapper.map(patient, PatientResponse.class))
                        .collect(Collectors.toList())
        );
    }

    private Patient findPatient(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
    }

    private void validateRequest(CreatePatientRequest request) {
        if (request == null) {
            throw new BadRequestException("Patient request is required");
        }

        if (request.getPatientAddress() == null) {
            throw new BadRequestException("Patient address is required");
        }

        if (Boolean.FALSE.equals(request.getIsCommAddressSameAsCardAddress()) && request.getPatientCommAddress() == null) {
            throw new BadRequestException("Communication address is required when addresses are different");
        }

        if (!request.getRelation().equalsIgnoreCase("SELF") && request.getSubscriber() == null) {
            throw new BadRequestException("Subscriber is required when relation is " + request.getRelation());
        }
    }

    private void applyAddressRule(Patient patient) {
        if (Boolean.TRUE.equals(patient.getIsCommAddressSameAsCardAddress())) {
            patient.setPatientCommAddress(null);
        }
    }


    @Override
    public ApiResponse searchPatientByKeyword(String keyword) {

        // Validate keyword
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ApiResponse(2, "Search keyword is required", null);
        }

        String searchKeyword = keyword.trim();

        // Optional: prevent unnecessarily short searches
        if (searchKeyword.length() < 2) {
            return new ApiResponse(2, "Search keyword must contain at least 2 characters", null);
        }

        try {
            List<Patient> patients = patientRepository.searchByKeyword(searchKeyword);

            if (patients == null || patients.isEmpty()) {
                return new ApiResponse(1, "No patients found", Collections.emptyList());
            }

            List<PatientResponse> patientResponses = patients.stream()
                    .map(patient -> modelMapper.map(patient, PatientResponse.class))
                    .toList();

            return new ApiResponse(1, "Patients found successfully", patientResponses, (long) patientResponses.size());

        } catch (Exception e) {
            log.error("Error while searching patients with keyword: {}", searchKeyword, e);
            return new ApiResponse(2, "Unable to search patients", null);
        }
    }

    @Override
    public ApiResponse getPatientStatusIfExists(String abhaId){
        if (!patientRepository.existsByAbhaNumber(abhaId)) {
            return new ApiResponse(2,"Patient not found!");
        }
        return new ApiResponse(1,"Patient found!",patientRepository.existsByAbhaNumberAndActiveTrue(abhaId));
    }
}
