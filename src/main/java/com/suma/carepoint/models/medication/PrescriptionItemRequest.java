package com.suma.carepoint.models.medication;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionItemRequest {

    private Long medicationId;

    private String dosage;

    private String frequency;

    private String duration;

    private String instructions;
}