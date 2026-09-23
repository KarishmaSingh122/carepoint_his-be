package com.suma.carepoint.models.medication;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateMedicationRequest {

    @NotBlank(message = "Medication name is required")
    @Size(max = 150, message = "Medication name cannot exceed 150 characters")
    private String name;

    @Size(max = 150, message = "Generic name cannot exceed 150 characters")
    private String genericName;

    @Size(max = 50, message = "Strength cannot exceed 50 characters")
    private String strength;
}
