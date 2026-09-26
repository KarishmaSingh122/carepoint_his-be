package com.suma.carepoint.services.document;

import com.suma.carepoint.entities.admission.Admission;
import com.suma.carepoint.entities.auth.User;
import com.suma.carepoint.entities.document.Document;
import com.suma.carepoint.entities.document.DocumentType;
import com.suma.carepoint.entities.patient.Patient;
import com.suma.carepoint.entities.visit.Visit;
import com.suma.carepoint.exceptions.BadRequestException;
import com.suma.carepoint.exceptions.ConflictException;
import com.suma.carepoint.exceptions.ResourceNotFoundException;
import com.suma.carepoint.models.document.DocumentRequest;
import com.suma.carepoint.models.document.DocumentResponse;
import com.suma.carepoint.models.mapper.DocumentMapper;
import com.suma.carepoint.models.utility.PageResponse;
import com.suma.carepoint.repositories.admisssion.AdmissionRepository;
import com.suma.carepoint.repositories.auth.UserRepository;
import com.suma.carepoint.repositories.document.DocumentRepository;
import com.suma.carepoint.repositories.patient.PatientRepository;
import com.suma.carepoint.repositories.visit.VisitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.suma.carepoint.models.constants.ApiConstant.Document.MAX_TOTAL_FILES_SIZE;
import static com.suma.carepoint.models.constants.ApiConstant.Document.ROOT_FOLDER;
import static com.suma.carepoint.models.utility.PageResponse.buildPageResponse;
import static com.suma.carepoint.models.utility.PageResponse.validatePagination;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;
    private final AdmissionRepository admissionRepository;
    private final UserRepository userRepository;
    private final DocumentMapper documentMapper;


    @Override
    public DocumentResponse upload(Long patientId, Long uploadedBy,
                                   DocumentRequest request, MultipartFile file) {
        validateFile(file);
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        User user = userRepository.findById(uploadedBy)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Visit visit = resolveVisit(request.getVisitId());

        Admission admission = resolveAdmission(request.getAdmissionId());
        if (visit == null && admission == null) {
            throw new ConflictException("Either visitId or admissionId is required");
        }
        if (visit != null && !visit.getPatient().getPatientId().equals(patientId)) {
            throw new ConflictException("Visit does not belong to patient");
        }
        if (admission != null && !admission.getPatient().getPatientId().equals(patientId)) {
            throw new ConflictException("Admission does not belong to patient");
        }

        String originalFileName =
                StringUtils.cleanPath(file.getOriginalFilename() == null ? "document" : file.getOriginalFilename());
        String extension = StringUtils.getFilenameExtension(originalFileName);
        String storedFileName = UUID.randomUUID() + (extension == null ? "" : "." + extension);
        Path patientDirectory = Paths.get(ROOT_FOLDER).toAbsolutePath().normalize().resolve(String.valueOf(patientId));
        Path targetPath = patientDirectory.resolve(storedFileName).normalize();
        if (!targetPath.startsWith(patientDirectory)) {
            throw new ConflictException("Invalid file path");
        }
        try {
            Files.createDirectories(patientDirectory);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            log.error("Failed to store document. patientId={}", patientId, exception);
            throw new IllegalStateException("Unable to store document");
        }
        Document document = Document.builder()
                .patient(patient).visit(visit)
                .admission(admission).uploadedBy(user)
                .documentType(request.getDocumentType())
                .fileName(originalFileName).filePath(targetPath.toString())
                .fileSize(file.getSize()).contentType(file.getContentType())
                .storedFileName(storedFileName).verified(true)
                .build();
        try {
            Document saved = documentRepository.save(document);
            log.info("Document uploaded successfully. documentId={}, patientId={}, uploadedBy={}",
                    saved.getDocumentId(), patientId, uploadedBy);
            return documentMapper.toResponse(saved);
        } catch (RuntimeException exception) {
            deleteStoredFile(targetPath);
            throw exception;
        }
    }

    @Override
    public DocumentResponse getById(Long documentId) {
        return documentMapper.toResponse(findDocument(documentId));
    }

    @Override
    public PageResponse getAll(Long patientId, Long visitId, Long admissionId,
                               DocumentType documentType, Boolean verified, int page, int size) {
        validatePagination(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "uploadedAt"));
        Page<Document> documents;

        if (patientId != null && documentType != null) {
            documents = documentRepository.findByPatientPatientIdAndDocumentType(patientId, documentType, pageable);
        } else if (patientId != null && verified != null) {
            documents = documentRepository.findByPatientPatientIdAndVerified(patientId, verified, pageable);
        } else if (patientId != null) {
            documents = documentRepository.findByPatientPatientId(patientId, pageable);
        } else if (visitId != null) {
            documents = documentRepository.findByVisitVisitId(visitId, pageable);
        } else if (admissionId != null) {
            documents = documentRepository.findByAdmissionAdmissionId(admissionId, pageable);
        } else if (documentType != null) {
            documents = documentRepository.findByDocumentType(documentType, pageable);
        } else {
            documents = documentRepository.findAll(pageable);
        }
        return buildPageResponse(
                documents,
                documents.getContent().stream().map(documentMapper::toResponse)
                        .collect(Collectors.toList()));
    }

    @Override
    public Resource download(Long documentId) {
        Document document = findDocument(documentId);
        Path path = Paths.get(document.getFilePath()).toAbsolutePath().normalize();
        Resource resource = new FileSystemResource(path);
        if (!resource.exists() || !resource.isReadable()) {
            throw new ResourceNotFoundException("Document file not found");
        }
        return resource;
    }

    @Override
    public DocumentResponse updateVerification(Long documentId, boolean verified) {
        Document document = findDocument(documentId);
        document.setVerified(verified);
        Document updated = documentRepository.save(document);
        log.info("Document verification updated. documentId={}, verified={}", documentId, verified);
        return documentMapper.toResponse(updated);
    }

    @Override
    public void delete(Long documentId) {
        Document document = findDocument(documentId);
        Path path = Paths.get(document.getFilePath()).toAbsolutePath().normalize();
        try {
            Files.deleteIfExists(path);
        } catch (IOException exception) {
            log.error("Failed to delete document file. documentId={}", documentId, exception);
            throw new IllegalStateException("Unable to delete document file");
        }
        documentRepository.delete(document);
        log.info("Document deleted successfully. documentId={}", documentId);
    }

    private Document findDocument(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));
    }

    private Visit resolveVisit(Long visitId) {
        if (visitId == null) {
            return null;
        }
        return visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found"));
    }

    private Admission resolveAdmission(Long admissionId) {
        if (admissionId == null) {
            return null;
        }
        return admissionRepository.findById(admissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Admission not found"));
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }
        if (file.getSize() <= 0) {
            throw new IllegalArgumentException("File must not be empty");
        }
        if (file.getOriginalFilename() == null || file.getOriginalFilename().isBlank()) {
            throw new IllegalArgumentException("File name is required");
        }
    }

    private void deleteStoredFile(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException exception) {
            log.error("Failed to clean up stored document file", exception);
        }
    }

    @Override
    public List<DocumentResponse> uploadDocuments(Long patientId, Long uploadedBy,
                                                  DocumentRequest request, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new BadRequestException("At least one file is required");
        }

        long totalFileSize = files.stream()
                .mapToLong(MultipartFile::getSize)
                .sum();
        if (totalFileSize > MAX_TOTAL_FILES_SIZE) {
            throw new BadRequestException("Total file size must not exceed " + (MAX_TOTAL_FILES_SIZE / (1024 * 1024L)) + " MB");
        }

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        User user = userRepository.findById(uploadedBy)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Visit visit = null;
        if (request.getVisitId() != null) {
            visit = visitRepository.findById(request.getVisitId())
                    .orElseThrow(() -> new ResourceNotFoundException("Visit not found"));
            if (!visit.getPatient().getPatientId().equals(patientId)) {
                throw new ConflictException("Visit does not belong to patient");
            }
        }
        Admission admission = null;
        if (request.getAdmissionId() != null) {
            admission = admissionRepository.findById(request.getAdmissionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Admission not found"));
            if (!admission.getPatient().getPatientId().equals(patientId)) {
                throw new ConflictException("Admission does not belong to patient");
            }
        }
        Path patientDirectory = Paths.get(ROOT_FOLDER, String.valueOf(patientId)).toAbsolutePath().normalize();
        try {
            Files.createDirectories(patientDirectory);
        } catch (IOException exception) {
            log.error("Unable to create document storage directory. patientId={}", patientId, exception);
            throw new BadRequestException("Unable to initialize document storage");
        }
        List<Document> documents = new ArrayList<>();
        List<Path> storedFiles = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                validateFile(file);
                String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                String extension = StringUtils.getFilenameExtension(originalFileName);
                String storedFileName = UUID.randomUUID().toString();
                if (StringUtils.hasText(extension)) {
                    storedFileName = storedFileName + "." + extension;
                }
                Path targetPath = patientDirectory.resolve(storedFileName).normalize();
                if (!targetPath.startsWith(patientDirectory)) {
                    throw new BadRequestException("Invalid file path");
                }
                Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                storedFiles.add(targetPath);
                Document document = Document.builder().patient(patient)
                        .visit(visit).admission(admission).uploadedBy(user)
                        .documentType(request.getDocumentType())
                        .fileName(originalFileName).filePath(targetPath.toString())
                        .fileSize(file.getSize()).contentType(file.getContentType())
                        .storedFileName(storedFileName).verified(true)
                        .build();
                documents.add(document);
            }
            List<Document> savedDocuments = documentRepository.saveAll(documents);
            log.info("Documents uploaded successfully. patientId={}, uploadedBy={}, count={}",
                    patientId, uploadedBy, savedDocuments.size());
            return savedDocuments.stream().map(documentMapper::toResponse).toList();
        } catch (IOException exception) {
            deleteFiles(storedFiles);
            log.error("Failed to store documents. patientId={}", patientId, exception);
            throw new BadRequestException("Unable to store documents");
        } catch (RuntimeException exception) {
            deleteFiles(storedFiles);
            throw exception;
        }
    }

    private void deleteFiles(List<Path> files) {
        for (Path path : files) {
            deleteStoredFile(path);
        }
    }
}