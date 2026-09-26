package com.suma.carepoint.services.document;

import com.suma.carepoint.entities.document.DocumentType;
import com.suma.carepoint.models.document.DocumentRequest;
import com.suma.carepoint.models.document.DocumentResponse;
import com.suma.carepoint.models.utility.PageResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {

    DocumentResponse upload(Long patientId, Long uploadedBy, DocumentRequest request, MultipartFile file);

    DocumentResponse getById(Long documentId);

    PageResponse getAll(Long patientId, Long visitId, Long admissionId,
                        DocumentType documentType, Boolean verified, int page, int size);

    Resource download(Long documentId);

    DocumentResponse updateVerification(Long documentId, boolean verified);

    void delete(Long documentId);

    List<DocumentResponse> uploadDocuments(Long patientId, Long uploadedBy, DocumentRequest request, List<MultipartFile> files);
}
