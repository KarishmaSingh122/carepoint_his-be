package com.suma.carepoint.repositories.document;

import com.suma.carepoint.entities.document.Document;
import com.suma.carepoint.entities.document.DocumentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    Page<Document> findByPatientPatientId(Long patientId, Pageable pageable);

    Page<Document> findByVisitVisitId(Long visitId, Pageable pageable);

    Page<Document> findByAdmissionAdmissionId(Long admissionId, Pageable pageable);

    Page<Document> findByUploadedByUserId(Long userId, Pageable pageable);

    Page<Document> findByDocumentType(DocumentType documentType, Pageable pageable);

    Page<Document> findByPatientPatientIdAndDocumentType(Long patientId, DocumentType documentType, Pageable pageable);

    Page<Document> findByPatientPatientIdAndVerified(Long patientId, boolean verified, Pageable pageable);
}
