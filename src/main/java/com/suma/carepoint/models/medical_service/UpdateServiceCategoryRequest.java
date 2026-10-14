package com.suma.carepoint.models.medical_service;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateServiceCategoryRequest {

    private String categoryName;

    private String description;

    private Boolean active;
}