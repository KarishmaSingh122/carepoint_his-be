package com.suma.carepoint.models.document;

import com.suma.carepoint.entities.document.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentRequest {

    private DocumentType documentType;
    private Long visitId;
    private Long admissionId;
}