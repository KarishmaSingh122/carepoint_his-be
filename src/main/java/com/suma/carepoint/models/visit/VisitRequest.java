package com.suma.carepoint.models.visit;

import com.suma.carepoint.entities.visit.VisitType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitRequest {

    @NotNull
    private Long patientId;

    private Long doctorId;

    private Long departmentId;

    @NotNull
    private VisitType visitType;

    private Instant visitDate;

    @Size(max = 5000)
    private String reason;
}