package com.suma.carepoint.controllers;


import com.suma.carepoint.entities.document.DocumentType;
import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.models.document.DocumentRequest;
import com.suma.carepoint.models.document.DocumentResponse;
import com.suma.carepoint.models.utility.PageResponse;
import com.suma.carepoint.services.document.DocumentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(ApiConstant.Document.BASE)
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping(value = ApiConstant.Document.UPLOAD,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> upload(
            @RequestParam @Min(1) Long patientId,
            @RequestParam @Min(1) Long uploadedBy,
            @Valid @ModelAttribute DocumentRequest request,
            @RequestPart("file") MultipartFile file) {
        DocumentResponse response = documentService.upload(patientId, uploadedBy, request, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(1, "", response));
    }

    @PostMapping(value = ApiConstant.Document.BULK_UPLOADS,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> uploadDocuments(
            @RequestParam @Min(1) Long patientId,
            @RequestParam @Min(1) Long uploadedBy,
            @Valid @ModelAttribute DocumentRequest request,
            @RequestParam("files") List<MultipartFile> files) {

        List<DocumentResponse> response =
                documentService.uploadDocuments(patientId, uploadedBy, request, files);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(HttpStatus.CREATED.value(),
                        "Documents uploaded successfully.", response));
    }


    @GetMapping(ApiConstant.Document.GET_BY_ID)
    public ResponseEntity<ApiResponse> getById(@PathVariable @Min(1) Long documentId) {

        DocumentResponse response = documentService.getById(documentId);
        return ResponseEntity.ok().body(new ApiResponse(1, "", response));
    }

    @GetMapping(ApiConstant.Document.GET_ALL)
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam(required = false) @Min(1) Long patientId,
            @RequestParam(required = false) @Min(1) Long visitId,
            @RequestParam(required = false) @Min(1) Long admissionId,
            @RequestParam(required = false) DocumentType documentType,
            @RequestParam(required = false) Boolean verified,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {

        PageResponse response =
                documentService.getAll(patientId, visitId, admissionId, documentType, verified, page, size);
        return ResponseEntity.ok().body(new ApiResponse(1, "", response));
    }

    @GetMapping(ApiConstant.Document.DOWNLOAD)
    public ResponseEntity<Resource> download(@PathVariable @Min(1) Long documentId) {
        Resource resource = documentService.download(documentId);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PatchMapping(ApiConstant.Document.VERIFICATION)
    public ResponseEntity<ApiResponse> updateVerification(
            @PathVariable @Min(1) Long documentId,
            @RequestParam boolean verified) {

        DocumentResponse response = documentService.updateVerification(documentId, verified);
        return ResponseEntity.ok().body(new ApiResponse(1, "", response));
    }

    @DeleteMapping(ApiConstant.Document.DELETE)
    public ResponseEntity<ApiResponse> delete(@PathVariable @Min(1) Long documentId) {
        documentService.delete(documentId);
        return ResponseEntity.ok().body(
                new ApiResponse(1, "Document deleted successfully", null));
    }
}
