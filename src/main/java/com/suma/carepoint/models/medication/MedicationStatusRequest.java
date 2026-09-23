package com.suma.carepoint.models.medication;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicationStatusRequest {

    @NotNull(message = "Active status is required")
    private Boolean active;
}