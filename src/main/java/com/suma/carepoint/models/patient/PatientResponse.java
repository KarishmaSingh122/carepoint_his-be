
package com.suma.carepoint.models.patient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.suma.carepoint.entities.patient.Address;
import com.suma.carepoint.entities.patient.ContactPerson;
import com.suma.carepoint.entities.patient.Gender;
import com.suma.carepoint.entities.patient.Subscriber;
import com.suma.carepoint.models.constants.ApiConstant;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponse {

    private Long patientId;

    private String abhaNumber;

    private String patientName;

    @JsonFormat(pattern = ApiConstant.DateTime.DATE_DD_MM_YYYY)
    private LocalDate patientDob;

    private String age;

    private String patientMob;

    private Gender gender;

    private String relation;

    private String patientMrNumber;

    private Boolean isCommAddressSameAsCardAddress;

    private Boolean isChildRegistration;

    private Address patientAddress;

    private Address patientCommAddress;

    private ContactPerson contactPerson;

    private Subscriber subscriber;
}
