package com.suma.carepoint.services.allergy;

import com.suma.carepoint.entities.allergy.Allergy;
import com.suma.carepoint.entities.patient.Patient;
import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.allergy.AllergyResponse;
import com.suma.carepoint.models.allergy.CreateAllergyRequest;
import com.suma.carepoint.repositories.allergy.AllergyRepository;
import com.suma.carepoint.repositories.patient.PatientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class AllergyServiceImpl implements AllergyService{

    private final AllergyRepository allergyRepository;
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    public AllergyServiceImpl(AllergyRepository allergyRepository, PatientRepository patientRepository,
            ModelMapper modelMapper) {

        this.allergyRepository = allergyRepository;
        this.patientRepository = patientRepository;
        this.modelMapper = modelMapper;
    }

    //Create
    @Override
    public ApiResponse createAllergy(CreateAllergyRequest request) {

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Allergy allergy = modelMapper.map(request, Allergy.class);

        allergy.setPatient(patient);

        if (request.getActive() == null) {
            allergy.setActive(true);
        }

        allergy.setCreatedAt(OffsetDateTime.now());

        Allergy savedAllergy =
                allergyRepository.save(allergy);

        return new ApiResponse(1, "Allergy created successfully", mapToResponse(savedAllergy));
    }


   // Get
    @Override
    public ApiResponse getAllergy(Long allergyId) {

        Allergy allergy = allergyRepository.findById(allergyId)
                        .orElseThrow(() -> new RuntimeException("Allergy not found"));

        return new ApiResponse(1, "Allergy fetched successfully", mapToResponse(allergy));
    }


    //GetAll

    @Override
    public ApiResponse getAllAllergies() {

        List<Allergy> allergies = allergyRepository.findAll();

        List<AllergyResponse> responses = allergies.stream()
                .map(this::mapToResponse).toList();

        return new ApiResponse(1, "Allergies fetched successfully", responses);
    }


    //Update
    @Override
    public ApiResponse updateAllergy(Long allergyId, CreateAllergyRequest request) {

        Allergy existingAllergy = allergyRepository.findById(allergyId)
                .orElseThrow(() -> new RuntimeException("Allergy not found"));

        Patient patient = patientRepository.findById(request.getPatientId())
                        .orElseThrow(() -> new RuntimeException("Patient not found"));

        modelMapper.map(request, existingAllergy);

        existingAllergy.setPatient(patient);

        Allergy updatedAllergy = allergyRepository.save(existingAllergy);

        return new ApiResponse(1, "Allergy updated successfully", mapToResponse(updatedAllergy));
    }

    //Delete
    @Override
    public ApiResponse deleteAllergy(Long allergyId) {

        Allergy allergy = allergyRepository.findById(allergyId)
                        .orElseThrow(() -> new RuntimeException("Allergy not found"));

        allergyRepository.delete(allergy);

        return new ApiResponse(1, "Allergy deleted successfully", null);
    }

    private AllergyResponse mapToResponse(Allergy allergy) {

        AllergyResponse response = modelMapper.map(allergy, AllergyResponse.class);

        response.setPatientId(allergy.getPatient().getPatientId());

        return response;
    }
}
