package com.suma.carepoint.services.medical_record;


import com.suma.carepoint.models.medical_record.MedicalRecordRequest;
import com.suma.carepoint.models.medical_record.MedicalRecordResponse;
import com.suma.carepoint.models.utility.PageResponse;

public interface MedicalRecordService {

    MedicalRecordResponse create(MedicalRecordRequest request);

    MedicalRecordResponse getById(Long medicalRecordId);

    PageResponse getAll(Long patientId, Long visitId,
                        Long doctorId, Long diagnosisId, int page, int size);

    MedicalRecordResponse update(Long medicalRecordId, MedicalRecordRequest request);

    void delete(Long medicalRecordId);
}
