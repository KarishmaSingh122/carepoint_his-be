package com.suma.carepoint.models.document;

import com.suma.carepoint.entities.document.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BulkDocumentItem {
/**
* This class created to resolved conflict between two same types of files. (ex. Now 1+ LAB_REPORT able to upload at a time.)
* */
    @NotBlank
    private String id;

    @NotNull
    private DocumentType documentType;
}
