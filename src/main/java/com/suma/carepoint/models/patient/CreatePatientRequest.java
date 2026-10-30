package com.suma.carepoint.models.patient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.suma.carepoint.entities.patient.Address;
import com.suma.carepoint.entities.patient.ContactPerson;
import com.suma.carepoint.entities.patient.Gender;
import com.suma.carepoint.entities.patient.Subscriber;
import com.suma.carepoint.models.constants.ApiConstant;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePatientRequest {

    @Pattern(regexp = "^[0-9]{14}$", message = "ABHA number must contain exactly 14 digits")
    @NotBlank(message = "ABHA number required.")
    private String abhaNumber;

    @Size(max = 200)
    private String patientName;

    @PastOrPresent(message = "Date of birth cannot be in the future")
    @JsonFormat(pattern = ApiConstant.DateTime.DATE_DD_MM_YYYY)
    private LocalDate patientDob;

    @Size(max = 20)
    private String patientMob;

    private Gender gender;

    @Size(max = 50)
    private String relation;

    @Size(max = 50)
    private String patientMrNumber;

    private Boolean isCommAddressSameAsCardAddress;

    private Boolean isChildRegistration;

    @Valid
    private Address patientAddress;

    @Valid
    private Address patientCommAddress;

    @Valid
    private ContactPerson contactPerson;

    @Valid
    private Subscriber subscriber;
}
