package com.suma.carepoint.services.emr;

import com.suma.carepoint.entities.emr.Diagnosis;
import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.emr.DiagnosisRequest;
import com.suma.carepoint.models.emr.DiagnosisResponse;
import com.suma.carepoint.repositories.emr.DiagnosisRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class DiagnosisServiceImpl implements DiagnosisService {

    private final ModelMapper modelMapper;
    private final DiagnosisRepository diagnosisRepository;

    public DiagnosisServiceImpl(ModelMapper modelMapper,
                                DiagnosisRepository diagnosisRepository) {
        this.modelMapper = modelMapper;
        this.diagnosisRepository = diagnosisRepository;
    }

    @Override
    @Transactional
    public ApiResponse createDiagnosis(DiagnosisRequest request) {

        try {

            if (request == null) {
                return new ApiResponse(
                        2,
                        "Diagnosis request cannot be null",
                        null
                );
            }

            // Check duplicate diagnosis code
            if (diagnosisRepository.existsByDiagnosisCodeIgnoreCase(
                    request.getDiagnosisCode())) {

                return new ApiResponse(
                        2,
                        "Diagnosis code already exists",
                        null
                );
            }

            // Check duplicate diagnosis name
            if (diagnosisRepository.existsByDiagnosisNameIgnoreCase(
                    request.getDiagnosisName())) {

                return new ApiResponse(
                        2,
                        "Diagnosis name already exists",
                        null
                );
            }

            Diagnosis diagnosis = modelMapper.map(
                    request,
                    Diagnosis.class
            );

            diagnosis.setDiagnosisCode(
                    request.getDiagnosisCode().trim()
            );

            diagnosis.setDiagnosisName(
                    request.getDiagnosisName().trim()
            );

            diagnosis.setActive(true);

            Diagnosis savedDiagnosis = diagnosisRepository.save(diagnosis);

            DiagnosisResponse response = buildDiagnosisResponse(
                    savedDiagnosis
            );

            return new ApiResponse(
                    1,
                    "Diagnosis created successfully",
                    response,
                    1L
            );

        } catch (Exception e) {

            log.error("Error while creating diagnosis", e);

            return new ApiResponse(
                    2,
                    "Failed to create diagnosis",
                    null
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse getDiagnosises() {

        try {

            List<Diagnosis> diagnoses =
                    diagnosisRepository.findAll();

            List<DiagnosisResponse> responses = diagnoses
                    .stream()
                    .map(this::buildDiagnosisResponse)
                    .toList();

            return new ApiResponse(
                    1,
                    "Diagnoses fetched successfully",
                    responses,
                    (long) responses.size()
            );

        } catch (Exception e) {

            log.error("Error while fetching diagnoses", e);

            return new ApiResponse(
                    2,
                    "Failed to fetch diagnoses",
                    null
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse getDiagnosisById(Long diagnosisId) {

        try {

            if (diagnosisId == null) {
                return new ApiResponse(
                        2,
                        "Diagnosis ID cannot be null",
                        null
                );
            }

            Diagnosis diagnosis = diagnosisRepository
                    .findById(diagnosisId)
                    .orElse(null);

            if (diagnosis == null) {
                return new ApiResponse(
                        2,
                        "Diagnosis not found",
                        null
                );
            }

            DiagnosisResponse response =
                    buildDiagnosisResponse(diagnosis);

            return new ApiResponse(
                    1,
                    "Diagnosis fetched successfully",
                    response,
                    1L
            );

        } catch (Exception e) {

            log.error(
                    "Error while fetching diagnosis with ID: {}",
                    diagnosisId,
                    e
            );

            return new ApiResponse(
                    2,
                    "Failed to fetch diagnosis",
                    null
            );
        }
    }

    @Override
    @Transactional
    public ApiResponse updateDiagnosis(
            Long diagnosisId,
            DiagnosisRequest request) {

        try {

            if (diagnosisId == null) {
                return new ApiResponse(
                        2,
                        "Diagnosis ID cannot be null",
                        null
                );
            }

            if (request == null) {
                return new ApiResponse(
                        2,
                        "Diagnosis request cannot be null",
                        null
                );
            }

            Diagnosis diagnosis = diagnosisRepository
                    .findById(diagnosisId)
                    .orElse(null);

            if (diagnosis == null) {
                return new ApiResponse(
                        2,
                        "Diagnosis not found",
                        null
                );
            }

            // Check duplicate diagnosis code
            if (diagnosisRepository
                    .existsByDiagnosisCodeIgnoreCaseAndDiagnosisIdNot(
                            request.getDiagnosisCode(),
                            diagnosisId)) {

                return new ApiResponse(
                        2,
                        "Diagnosis code already exists",
                        null
                );
            }

            // Check duplicate diagnosis name
            if (diagnosisRepository
                    .existsByDiagnosisNameIgnoreCaseAndDiagnosisIdNot(
                            request.getDiagnosisName(),
                            diagnosisId)) {

                return new ApiResponse(
                        2,
                        "Diagnosis name already exists",
                        null
                );
            }

            diagnosis.setDiagnosisCode(
                    request.getDiagnosisCode().trim()
            );

            diagnosis.setDiagnosisName(
                    request.getDiagnosisName().trim()
            );

            diagnosis.setDescription(
                    request.getDescription()
            );

            Diagnosis updatedDiagnosis =
                    diagnosisRepository.save(diagnosis);

            DiagnosisResponse response =
                    buildDiagnosisResponse(updatedDiagnosis);

            return new ApiResponse(
                    1,
                    "Diagnosis updated successfully",
                    response,
                    1L
            );

        } catch (Exception e) {

            log.error(
                    "Error while updating diagnosis with ID: {}",
                    diagnosisId,
                    e
            );

            return new ApiResponse(
                    2,
                    "Failed to update diagnosis",
                    null
            );
        }
    }

    @Override
    @Transactional
    public ApiResponse updateDiagnosisStatus(
            Long diagnosisId,
            Boolean active) {

        try {

            if (diagnosisId == null) {
                return new ApiResponse(
                        2,
                        "Diagnosis ID cannot be null",
                        null
                );
            }

            if (active == null) {
                return new ApiResponse(
                        2,
                        "Active status cannot be null",
                        null
                );
            }

            Diagnosis diagnosis = diagnosisRepository
                    .findById(diagnosisId)
                    .orElse(null);

            if (diagnosis == null) {
                return new ApiResponse(
                        2,
                        "Diagnosis not found",
                        null
                );
            }

            diagnosis.setActive(active);

            Diagnosis updatedDiagnosis =
                    diagnosisRepository.save(diagnosis);

            DiagnosisResponse response =
                    buildDiagnosisResponse(updatedDiagnosis);

            String message = Boolean.TRUE.equals(active)
                    ? "Diagnosis activated successfully"
                    : "Diagnosis deactivated successfully";

            return new ApiResponse(
                    1,
                    message,
                    response,
                    1L
            );

        } catch (Exception e) {

            log.error(
                    "Error while updating diagnosis status with ID: {}",
                    diagnosisId,
                    e
            );

            return new ApiResponse(
                    2,
                    "Failed to update diagnosis status",
                    null
            );
        }
    }

    @Override
    @Transactional
    public ApiResponse deleteDiagnosis(Long diagnosisId) {

        try {

            if (diagnosisId == null) {
                return new ApiResponse(
                        2,
                        "Diagnosis ID cannot be null",
                        null
                );
            }

            Diagnosis diagnosis = diagnosisRepository
                    .findById(diagnosisId)
                    .orElse(null);

            if (diagnosis == null) {
                return new ApiResponse(
                        2,
                        "Diagnosis not found",
                        null
                );
            }

            diagnosisRepository.delete(diagnosis);

            return new ApiResponse(
                    1,
                    "Diagnosis deleted successfully",
                    null
            );

        } catch (Exception e) {

            log.error(
                    "Error while deleting diagnosis with ID: {}",
                    diagnosisId,
                    e
            );

            return new ApiResponse(
                    2,
                    "Failed to delete diagnosis",
                    null
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse searchDiagnosises(String keyword) {

        try {

            if (keyword == null || keyword.trim().isEmpty()) {
                return new ApiResponse(
                        2,
                        "Search keyword cannot be empty",
                        null
                );
            }

            String searchKeyword = keyword.trim();

            List<Diagnosis> diagnoses =
                    diagnosisRepository.searchActiveDiagnoses(
                            searchKeyword
                    );

            List<DiagnosisResponse> responses = diagnoses
                    .stream()
                    .map(this::buildDiagnosisResponse)
                    .toList();

            return new ApiResponse(
                    1,
                    "Diagnoses searched successfully",
                    responses,
                    (long) responses.size()
            );

        } catch (Exception e) {

            log.error(
                    "Error while searching diagnoses with keyword: {}",
                    keyword,
                    e
            );

            return new ApiResponse(
                    2,
                    "Failed to search diagnoses",
                    null
            );
        }
    }

    private DiagnosisResponse buildDiagnosisResponse(
            Diagnosis diagnosis) {

        return DiagnosisResponse.builder()
                .diagnosisId(diagnosis.getDiagnosisId())
                .diagnosisCode(diagnosis.getDiagnosisCode())
                .diagnosisName(diagnosis.getDiagnosisName())
                .description(diagnosis.getDescription())
                .active(diagnosis.getActive())
                .createdAt(diagnosis.getCreatedAt())
                .updatedAt(diagnosis.getUpdatedAt())
                .build();
    }
}