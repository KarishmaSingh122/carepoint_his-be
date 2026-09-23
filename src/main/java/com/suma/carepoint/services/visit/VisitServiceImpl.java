package com.suma.carepoint.services.visit;

import com.suma.carepoint.entities.organization.Department;
import com.suma.carepoint.entities.organization.Staff;
import com.suma.carepoint.entities.patient.Patient;
import com.suma.carepoint.entities.visit.Visit;
import com.suma.carepoint.entities.visit.VisitStatus;
import com.suma.carepoint.exceptions.ConflictException;
import com.suma.carepoint.exceptions.ResourceNotFoundException;
import com.suma.carepoint.models.mapper.VisitMapper;
import com.suma.carepoint.models.utility.PageResponse;
import com.suma.carepoint.models.visit.VisitRequest;
import com.suma.carepoint.models.visit.VisitResponse;
import com.suma.carepoint.repositories.organization.DepartmentRepository;
import com.suma.carepoint.repositories.organization.StaffRepository;
import com.suma.carepoint.repositories.patient.PatientRepository;
import com.suma.carepoint.repositories.visit.VisitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;
    private final StaffRepository staffRepository;
    private final DepartmentRepository departmentRepository;
    private final VisitMapper visitMapper;

    @Override
    public VisitResponse create(VisitRequest request) {

        Patient patient = patientRepository.findById(
                request.getPatientId()
        ).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Patient not found"
                )
        );

        Staff doctor = resolveDoctor(
                request.getDoctorId()
        );

        Department department = resolveDepartment(
                request.getDepartmentId()
        );

        Visit visit = visitMapper.toEntity(request);

        visit.setPatient(patient);
        visit.setDoctor(doctor);
        visit.setDepartment(department);
        visit.setVisitType(request.getVisitType());
        visit.setVisitDate(
                request.getVisitDate() != null
                        ? request.getVisitDate()
                        : Instant.now()
        );
        visit.setStatus(VisitStatus.OPEN);

        Visit savedVisit = visitRepository.save(visit);

        log.info(
                "Visit created successfully. visitId={}, patientId={}, doctorId={}, departmentId={}",
                savedVisit.getVisitId(),
                patient.getPatientId(),
                doctor != null ? doctor.getStaffId() : null,
                department != null ? department.getDepartmentId() : null
        );

        return visitMapper.toResponse(savedVisit);
    }

    @Override
    @Transactional(readOnly = true)
    public VisitResponse getById(Long visitId) {

        Visit visit = findVisit(visitId);

        return visitMapper.toResponse(visit);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse getAll(
            Long patientId,
            Long doctorId,
            Long departmentId,
            String status,
            int page,
            int size
    ) {

        validatePagination(page, size);

        VisitStatus visitStatus =
                parseStatus(status);

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Direction.DESC,
                        "visitDate"
                )
        );

        Page<Visit> visits;

        if (patientId != null && visitStatus != null) {

            visits =
                    visitRepository
                            .findByPatientPatientIdAndStatus(
                                    patientId,
                                    visitStatus,
                                    pageable
                            );

        } else if (doctorId != null && visitStatus != null) {

            visits =
                    visitRepository
                            .findByDoctorStaffIdAndStatus(
                                    doctorId,
                                    visitStatus,
                                    pageable
                            );

        } else if (departmentId != null && visitStatus != null) {

            visits =
                    visitRepository
                            .findByDepartmentDepartmentIdAndStatus(
                                    departmentId,
                                    visitStatus,
                                    pageable
                            );

        } else if (patientId != null) {

            visits =
                    visitRepository.findByPatientPatientId(
                            patientId,
                            pageable
                    );

        } else if (doctorId != null) {

            visits =
                    visitRepository.findByDoctorStaffId(
                            doctorId,
                            pageable
                    );

        } else if (departmentId != null) {

            visits =
                    visitRepository.findByDepartmentDepartmentId(
                            departmentId,
                            pageable
                    );

        } else if (visitStatus != null) {

            visits =
                    visitRepository.findByStatus(
                            visitStatus,
                            pageable
                    );

        } else {

            visits = visitRepository.findAll(pageable);
        }

        return PageResponse.builder()
                .content(
                        visits.getContent()
                                .stream()
                                .map(visitMapper::toResponse)
                                .collect(Collectors.toList())
                )
                .page(visits.getNumber())
                .size(visits.getSize())
                .totalElements(
                        visits.getTotalElements()
                )
                .totalPages(
                        visits.getTotalPages()
                )
                .first(visits.isFirst())
                .last(visits.isLast())
                .build();
    }

    @Override
    public VisitResponse update(
            Long visitId,
            VisitRequest request
    ) {

        Visit visit = findVisit(visitId);

        validateEditableStatus(visit);

        Patient patient = patientRepository.findById(
                request.getPatientId()
        ).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Patient not found"
                )
        );

        Staff doctor = resolveDoctor(
                request.getDoctorId()
        );

        Department department = resolveDepartment(
                request.getDepartmentId()
        );

        visit.setPatient(patient);
        visit.setDoctor(doctor);
        visit.setDepartment(department);
        visit.setVisitType(request.getVisitType());

        if (request.getVisitDate() != null) {
            visit.setVisitDate(
                    request.getVisitDate()
            );
        }

        visit.setReason(request.getReason());

        Visit updatedVisit =
                visitRepository.save(visit);

        log.info(
                "Visit updated successfully. visitId={}",
                visitId
        );

        return visitMapper.toResponse(updatedVisit);
    }

    @Override
    public VisitResponse updateStatus(
            Long visitId,
            String status
    ) {
        VisitStatus requestedStatus = VisitStatus.valueOf(status);
        Visit visit = findVisit(visitId);

        VisitStatus currentStatus =
                visit.getStatus();

        validateStatusTransition(
                currentStatus,
                requestedStatus
        );

        visit.setStatus(requestedStatus);

        Visit updatedVisit =
                visitRepository.save(visit);

        log.info(
                "Visit status updated successfully. visitId={}, status={}",
                visitId,
                requestedStatus
        );

        return visitMapper.toResponse(updatedVisit);
    }

    private Visit findVisit(Long visitId) {

        return visitRepository.findById(visitId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Visit not found"
                        )
                );
    }

    private Staff resolveDoctor(Long doctorId) {

        if (doctorId == null) {
            return null;
        }

        return staffRepository.findById(doctorId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Doctor not found"
                        )
                );
    }

    private Department resolveDepartment(
            Long departmentId
    ) {

        if (departmentId == null) {
            return null;
        }

        return departmentRepository.findById(departmentId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Department not found"
                        )
                );
    }

    private VisitStatus parseStatus(String status) {

        if (status == null || status.isBlank()) {
            return null;
        }

        try {
            return VisitStatus.valueOf(
                    status.trim().toUpperCase()
            );
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Invalid visit status"
            );
        }
    }

    private void validateStatusTransition(
            VisitStatus currentStatus,
            VisitStatus requestedStatus
    ) {

        if (currentStatus == VisitStatus.COMPLETED
                && requestedStatus != VisitStatus.COMPLETED) {

            throw new ConflictException(
                    "Completed visit status cannot be changed"
            );
        }

        if (currentStatus == VisitStatus.CANCELLED
                && requestedStatus != VisitStatus.CANCELLED) {

            throw new ConflictException(
                    "Cancelled visit status cannot be changed"
            );
        }
    }

    private void validateEditableStatus(
            Visit visit
    ) {

        if (visit.getStatus() == VisitStatus.COMPLETED
                || visit.getStatus() == VisitStatus.CANCELLED) {

            throw new ConflictException(
                    "Completed or cancelled visits cannot be updated"
            );
        }
    }

    private void validatePagination(
            int page,
            int size
    ) {

        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page must be greater than or equal to 0"
            );
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Size must be between 1 and 100"
            );
        }
    }
}