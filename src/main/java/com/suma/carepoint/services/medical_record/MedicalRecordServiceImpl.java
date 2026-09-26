package com.suma.carepoint.services.medical_record;

import com.suma.carepoint.entities.emr.Diagnosis;
import com.suma.carepoint.entities.organization.Staff;
import com.suma.carepoint.entities.patient.Patient;
import com.suma.carepoint.entities.visit.Visit;
import com.suma.carepoint.exceptions.BadRequestException;
import com.suma.carepoint.exceptions.ConflictException;
import com.suma.carepoint.exceptions.ResourceNotFoundException;
import com.suma.carepoint.entities.medical_record.MedicalRecord;
import com.suma.carepoint.models.utility.PageResponse;
import com.suma.carepoint.repositories.emr.DiagnosisRepository;
import com.suma.carepoint.repositories.medical_record.MedicalRecordRepository;
import com.suma.carepoint.models.medical_record.MedicalRecordRequest;
import com.suma.carepoint.models.medical_record.MedicalRecordResponse;
import com.suma.carepoint.models.mapper.MedicalRecordMapper;
import com.suma.carepoint.repositories.organization.StaffRepository;
import com.suma.carepoint.repositories.patient.PatientRepository;
import com.suma.carepoint.repositories.visit.VisitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static com.suma.carepoint.models.utility.PageResponse.buildPageResponse;
import static com.suma.carepoint.models.utility.PageResponse.validatePagination;
import static com.suma.carepoint.models.utility.TextNormalizationUtils.normalize;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;
    private final StaffRepository staffRepository;
    private final DiagnosisRepository diagnosisRepository;
    private final MedicalRecordMapper medicalRecordMapper;

    @Override
    public MedicalRecordResponse create(MedicalRecordRequest request) {

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Visit visit = resolveVisit(request.getVisitId());
        Staff doctor = resolveDoctor(request.getDoctorId());
        Diagnosis diagnosis = resolveDiagnosis(request.getDiagnosisId());
        validateVisitPatient(visit, patient);

        MedicalRecord medicalRecord =
                MedicalRecord.builder()
                        .patient(patient)
                        .visit(visit)
                        .doctor(doctor)
                        .diagnosis(diagnosis)
                        .recordDate(request.getRecordDate() != null
                                ? request.getRecordDate()
                                : Instant.now())
                        .symptoms(normalize(request.getSymptoms()))
                        .treatment(normalize(request.getTreatment()))
                        .notes(normalize(request.getNotes()))
                        .build();
        try {
            MedicalRecord saved = medicalRecordRepository.save(medicalRecord);

            log.info(
                    "Medical record created successfully. medicalRecordId={}, patientId={}, visitId={}, doctorId={}",
                    saved.getMedicalRecordId(), patient.getPatientId(),
                    visit != null ? visit.getVisitId() : null,
                    doctor != null ? doctor.getStaffId() : null
            );
            return medicalRecordMapper.toResponse(saved);

        } catch (DataIntegrityViolationException exception) {
            log.error("Medical record creation failed. patientId={}", patient.getPatientId(), exception);
            throw new ConflictException("Medical record could not be created");
        }
    }

    @Override
    public MedicalRecordResponse getById(Long medicalRecordId) {
        return medicalRecordMapper.toResponse(findMedicalRecord(medicalRecordId));
    }

    @Override
    public PageResponse getAll(Long patientId, Long visitId,
                               Long doctorId, Long diagnosisId, int page, int size) {

        validatePagination(page, size);
        validatePositiveId(patientId, "patientId");
        validatePositiveId(visitId, "visitId");
        validatePositiveId(doctorId, "doctorId");
        validatePositiveId(diagnosisId, "diagnosisId");

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "recordDate"));
        Page<MedicalRecord> records =
                medicalRecordRepository.search(patientId, visitId, doctorId, diagnosisId, pageable);

        return buildPageResponse(records, records.getContent()
                .stream()
                .map(medicalRecordMapper::toResponse)
                .toList());
    }

    @Override
    public MedicalRecordResponse update(Long medicalRecordId, MedicalRecordRequest request) {

        MedicalRecord medicalRecord = findMedicalRecord(medicalRecordId);
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Visit visit = resolveVisit(request.getVisitId());
        Staff doctor = resolveDoctor(request.getDoctorId());
        Diagnosis diagnosis = resolveDiagnosis(request.getDiagnosisId());
        validateVisitPatient(visit, patient);

        medicalRecord.setPatient(patient);
        medicalRecord.setVisit(visit);
        medicalRecord.setDoctor(doctor);
        medicalRecord.setDiagnosis(diagnosis);

        if (request.getRecordDate() != null) {
            medicalRecord.setRecordDate(request.getRecordDate());
        }
        medicalRecord.setSymptoms(normalize(request.getSymptoms()));
        medicalRecord.setTreatment(normalize(request.getTreatment()));
        medicalRecord.setNotes(normalize(request.getNotes()));
        try {
            MedicalRecord updated = medicalRecordRepository.save(medicalRecord);
            log.info("Medical record updated successfully. medicalRecordId={}, patientId={}",
                    updated.getMedicalRecordId(), updated.getPatient().getPatientId());
            return medicalRecordMapper.toResponse(updated);
        } catch (DataIntegrityViolationException exception) {
            log.error("Medical record update failed. medicalRecordId={}", medicalRecordId, exception);
            throw new ConflictException("Medical record could not be updated");
        }
    }

    @Override
    public void delete(Long medicalRecordId) {

        MedicalRecord medicalRecord = findMedicalRecord(medicalRecordId);
        try {
            medicalRecordRepository.delete(medicalRecord);
            log.info("Medical record deleted successfully. medicalRecordId={}", medicalRecordId);
        } catch (DataIntegrityViolationException exception) {
            log.error("Medical record deletion failed. medicalRecordId={}", medicalRecordId, exception);
            throw new ConflictException("Medical record cannot be deleted");
        }
    }

    private MedicalRecord findMedicalRecord(Long medicalRecordId) {
        return medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found"));
    }

    private Visit resolveVisit(Long visitId) {

        if (visitId == null) {
            return null;
        }
        return visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found"));
    }

    private Staff resolveDoctor(Long doctorId) {
        if (doctorId == null) {
            return null;
        }
        return staffRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
    }

    private Diagnosis resolveDiagnosis(Long diagnosisId) {

        if (diagnosisId == null) {
            return null;
        }
        return diagnosisRepository.findById(diagnosisId)
                .orElseThrow(() -> new ResourceNotFoundException("Diagnosis not found"));
    }

    private void validateVisitPatient(Visit visit, Patient patient) {
        if (visit == null) {
            return;
        }
        if (!visit.getPatient().getPatientId().equals(patient.getPatientId())) {
            throw new BadRequestException("Visit does not belong to the specified patient");
        }
    }

    private void validatePositiveId(Long value, String fieldName) {
        if (value != null && value < 1) {
            throw new BadRequestException(fieldName + " must be greater than 0");
        }
    }
}
