package com.suma.carepoint.models.medication;


import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicationResponse {

    private Long medicationId;

    private String name;

    private String genericName;

    private String strength;

    private Boolean active;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}