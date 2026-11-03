package com.suma.carepoint.models.patient;

import com.suma.carepoint.entities.patient.PatientPolicyDetails;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientUpdateRequest {

    @Valid
    private CreatePatientRequest patientDetails;
    private PatientPolicyDetails patientPolicyDetails;

}
