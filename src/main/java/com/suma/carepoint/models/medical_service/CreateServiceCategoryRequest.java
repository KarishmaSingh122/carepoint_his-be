package com.suma.carepoint.models.medical_service;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateServiceCategoryRequest {

    private String categoryName;

    private String description;
}
