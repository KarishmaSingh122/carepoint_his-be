package com.suma.carepoint.models.mapper;

import com.suma.carepoint.entities.medical_record.MedicalRecord;
import com.suma.carepoint.models.medical_record.MedicalRecordResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MedicalRecordMapper {

    private final ModelMapper modelMapper;

    public MedicalRecordResponse toResponse(MedicalRecord medicalRecord) {

        MedicalRecordResponse response = modelMapper.map(medicalRecord, MedicalRecordResponse.class);

        response.setPatientId(medicalRecord.getPatient() != null
                ? medicalRecord.getPatient().getPatientId() : null);

        response.setVisitId(medicalRecord.getVisit() != null
                ? medicalRecord.getVisit().getVisitId() : null);

        response.setDoctorId(medicalRecord.getDoctor() != null
                ? medicalRecord.getDoctor().getStaffId() : null);

        response.setDiagnosisId(medicalRecord.getDiagnosis() != null
                ? medicalRecord.getDiagnosis().getDiagnosisId() : null);

        return response;
    }
}
