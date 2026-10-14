package com.suma.carepoint.models.medication;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionItemResponse {

    private Long prescriptionItemId;

    private Long medicationId;

    private String medicationName;

    private String genericName;

    private String strength;

    private String dosage;

    private String frequency;

    private String duration;

    private String instructions;
}