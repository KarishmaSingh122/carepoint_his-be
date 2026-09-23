package com.suma.carepoint.models.medication;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionResponse {

    private Long prescriptionId;

    private Long patientId;

    private Long visitId;

    private Long doctorId;

    private OffsetDateTime prescriptionDate;

    private String notes;

    private Boolean active;

    private OffsetDateTime createdAt;

    private List<PrescriptionItemResponse> items;


}