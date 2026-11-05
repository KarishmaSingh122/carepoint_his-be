package com.suma.carepoint.entities.patient;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactPerson {

    private String name;
    private String relationDescription;
    private String relationCode;
    private String relationUrl;
    private String address;
    private String mobileNumber;
    private Gender gender;
}
