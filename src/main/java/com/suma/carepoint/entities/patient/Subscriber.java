package com.suma.carepoint.entities.patient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.suma.carepoint.models.constants.ApiConstant;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscriber {

    private String abhaNumber;
    private String name;
    private String gender;
    private String mob;

    @JsonFormat(pattern = ApiConstant.DateTime.DATE_DD_MM_YYYY)
    private LocalDate dob;
    private String subscriberId;
}
