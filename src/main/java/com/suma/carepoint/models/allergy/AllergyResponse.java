package com.suma.carepoint.models.allergy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AllergyResponse {
    private Long allergyId;

    private Long patientId;

    private String allergyName;

    private String reaction;

    private String severity;

    private Boolean active;

    private OffsetDateTime createdAt;
}
