package com.suma.carepoint.models.mapper;

import com.suma.carepoint.entities.document.Document;
import com.suma.carepoint.models.document.DocumentResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DocumentMapper {

    private final ModelMapper modelMapper;

    public DocumentResponse toResponse(Document document) {

        DocumentResponse response = modelMapper.map(document, DocumentResponse.class);
        if (document.getPatient() != null) {
            response.setPatientId(document.getPatient().getPatientId());
        }
        if (document.getVisit() != null) {
            response.setVisitId(document.getVisit().getVisitId());
        }
        if (document.getAdmission() != null) {
            response.setAdmissionId(document.getAdmission().getAdmissionId());
        }
        if (document.getUploadedBy() != null) {
            response.setUploadedBy(document.getUploadedBy().getUserId());
        }

        return response;
    }
}