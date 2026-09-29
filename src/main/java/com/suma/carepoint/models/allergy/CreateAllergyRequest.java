package com.suma.carepoint.models.allergy;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateAllergyRequest {
    private Long patientId;

    private String allergyName;

    private String reaction;

    private String severity;

    private Boolean active;
}
