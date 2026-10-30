package com.suma.carepoint.entities.patient;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    private String address;
    private String pincode;
    private String state;
    private String district;
    private String villageTownCity;
//    private String country;
}