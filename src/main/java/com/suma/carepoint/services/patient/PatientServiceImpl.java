package com.suma.carepoint.services.patient;

import com.suma.carepoint.entities.patient.*;
import com.suma.carepoint.exceptions.BadRequestException;
import com.suma.carepoint.exceptions.ConflictException;
import com.suma.carepoint.exceptions.ResourceNotFoundException;
import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.patient.CreatePatientRequest;
import com.suma.carepoint.models.patient.PatientResponse;
import com.suma.carepoint.models.patient.PatientUpdateRequest;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
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
    private final JdbcTemplate jdbcTemplate;

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
        patient.setPatientMrNumber(generateMrNumber());
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
    public ApiResponse updatePatient(Long patientId, PatientUpdateRequest requestPayload) {

        CreatePatientRequest request = requestPayload.getPatientDetails();
        PatientPolicyDetails policyDetails = requestPayload.getPatientPolicyDetails();

        Patient patient = findPatient(patientId);
        patientRepository.findByAbhaNumber(request.getAbhaNumber())
                .filter(existing -> !existing.getPatientId().equals(patientId))
                .ifPresent(existing -> {
                    throw new ConflictException("Patient with ABHA number already exists");
                });

        patient.setAbhaNumber(request.getAbhaNumber());
        applyAddressRule(patient);

        if (request.getPatientName() != null)
            patient.setPatientName(request.getPatientName());

        if (request.getPatientDob() != null)
            patient.setPatientDob(request.getPatientDob());

        if (request.getPatientMob() != null)
            patient.setPatientMob(request.getPatientMob());

        if (request.getGender() != null)
            patient.setGender(request.getGender());

        if (request.getRelation() != null)
            patient.setRelation(request.getRelation());

        if (request.getIsCommAddressSameAsCardAddress() != null)
            patient.setIsCommAddressSameAsCardAddress(request.getIsCommAddressSameAsCardAddress());

        if (request.getIsChildRegistration() != null)
            patient.setIsChildRegistration(request.getIsChildRegistration());

        if (request.getPatientAddress() != null) {
            if (patient.getPatientAddress() == null) {
                patient.setPatientAddress(new Address());
            }
            updateAddress(patient.getPatientAddress(), request.getPatientAddress());
        }

        if (request.getPatientCommAddress() != null) {
            if (patient.getPatientCommAddress() == null) {
                patient.setPatientCommAddress(new Address());
            }
            updateAddress(patient.getPatientCommAddress(), request.getPatientCommAddress());
        }

        if (request.getContactPerson() != null) {
            updateContactPerson(patient, request.getContactPerson());
        }

        if (request.getSubscriber() != null) {
            updateSubscriber(patient, request.getSubscriber());
        }

        if (policyDetails != null) {
            updatePolicyDetails(patientId, policyDetails);
        }
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

    private String generateMrNumber() {
        Long sequence = jdbcTemplate.queryForObject("SELECT nextval('patient_mr_sequence')", Long.class);
        if (sequence == null || sequence <= 0) {
            throw new IllegalStateException("Failed to generate Patient MR Number");
        }

        int year = LocalDate.now(Clock.system(ZoneId.of("Asia/Kolkata"))).getYear();
        return String.format("MR-%04d-%06d", year, sequence);
    }

    private void updatePatientFields(Patient patient, CreatePatientRequest request) {

        if (request.getAbhaNumber() != null) {
            patient.setAbhaNumber(request.getAbhaNumber());
        }

        if (request.getPatientName() != null) {
            patient.setPatientName(request.getPatientName());
        }

        if (request.getPatientDob() != null) {
            patient.setPatientDob(request.getPatientDob());
        }

        if (request.getPatientMob() != null) {
            patient.setPatientMob(request.getPatientMob());
        }

        if (request.getGender() != null) {
            patient.setGender(request.getGender());
        }

        if (request.getRelation() != null) {
            patient.setRelation(request.getRelation());
        }

        if (request.getIsCommAddressSameAsCardAddress() != null) {
            patient.setIsCommAddressSameAsCardAddress(request.getIsCommAddressSameAsCardAddress());
        }

        if (request.getIsChildRegistration() != null) {
            patient.setIsChildRegistration(request.getIsChildRegistration());
        }

        if (request.getPatientAddress() != null) {
            updateAddress(patient.getPatientAddress(), request.getPatientAddress());
        }

        if (request.getPatientCommAddress() != null) {
            updateAddress(patient.getPatientCommAddress(), request.getPatientCommAddress());
        }

        if (request.getContactPerson() != null) {
            updateContactPerson(patient, request.getContactPerson());
        }

        if (request.getSubscriber() != null) {
            updateSubscriber(patient, request.getSubscriber());
        }
    }

    private void updatePolicyDetails(Long patientId, PatientPolicyDetails request) {

//        PatientPolicyDetails existing = policyRepository
//                .findByPatientPatientId(patientId)
//                .orElseGet(PatientPolicyDetails::new);
//
//        modelMapper.map(request, existing);
//
//        policyRepository.save(existing);
    }

    private void updateAddress(Address existing, Address request) {

        if (request == null) {
            return;
        }

        if (existing == null) {
            existing = new Address();
        }

        if (request.getAddress() != null) {
            existing.setAddress(request.getAddress());
        }

        if (request.getPincode() != null) {
            existing.setPincode(request.getPincode());
        }

        if (request.getState() != null) {
            existing.setState(request.getState());
        }

        if (request.getDistrict() != null) {
            existing.setDistrict(request.getDistrict());
        }

        if (request.getVillageTownCity() != null) {
            existing.setVillageTownCity(request.getVillageTownCity());
        }
    }

    private void updateContactPerson(Patient patient, ContactPerson request) {

        if (request == null) {
            return;
        }

        if (patient.getContactPerson() == null) {
            patient.setContactPerson(new ContactPerson());
        }

        ContactPerson existing = patient.getContactPerson();

        if (request.getName() != null) {
            existing.setName(request.getName());
        }

        if (request.getRelationDescription() != null) {
            existing.setRelationDescription(request.getRelationDescription());
        }

        if (request.getRelationCode() != null) {
            existing.setRelationCode(request.getRelationCode());
        }

        if (request.getRelationUrl() != null) {
            existing.setRelationUrl(request.getRelationUrl());
        }

        if (request.getAddress() != null) {
            existing.setAddress(request.getAddress());
        }

        if (request.getMobileNumber() != null) {
            existing.setMobileNumber(request.getMobileNumber());
        }

        if (request.getGender() != null) {
            existing.setGender(request.getGender());
        }
    }

    private void updateSubscriber(Patient patient, Subscriber request) {

        if (request == null) {
            return;
        }

        if (patient.getSubscriber() == null) {
            patient.setSubscriber(new Subscriber());
        }

        Subscriber existing = patient.getSubscriber();

        if (request.getAbhaNumber() != null) {
            existing.setAbhaNumber(request.getAbhaNumber());
        }

        if (request.getName() != null) {
            existing.setName(request.getName());
        }

        if (request.getGender() != null) {
            existing.setGender(request.getGender());
        }

        if (request.getMob() != null) {
            existing.setMob(request.getMob());
        }

        if (request.getDob() != null) {
            existing.setDob(request.getDob());
        }

        if (request.getSubscriberId() != null) {
            existing.setSubscriberId(request.getSubscriberId());
        }
    }
}
