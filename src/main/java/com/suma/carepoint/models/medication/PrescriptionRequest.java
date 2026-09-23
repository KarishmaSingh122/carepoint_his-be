package com.suma.carepoint.models.medication;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionRequest {

    private Long patientId;

    private Long visitId;

    private Long doctorId;

    private OffsetDateTime prescriptionDate;

    private String notes;

    private List<PrescriptionItemRequest> items;
}