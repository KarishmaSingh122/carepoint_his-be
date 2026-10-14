package com.suma.carepoint.models.document;

import com.suma.carepoint.entities.document.DocumentType;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentResponse {

    private Long documentId;
    private Long patientId;
    private Long visitId;
    private Long admissionId;
    private Long uploadedBy;
    private DocumentType documentType;
    private String fileName;
    private Long fileSize;
    private String contentType;
    private String storedFileName;
    private Instant createdAt;
    private Instant uploadedAt;
    private boolean verified;
}