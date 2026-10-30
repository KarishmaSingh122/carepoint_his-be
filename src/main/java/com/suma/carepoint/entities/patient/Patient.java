package com.suma.carepoint.entities.patient;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.suma.carepoint.entities.base.BaseEntity;
import com.suma.carepoint.models.constants.ApiConstant;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.EmbeddedColumnNaming;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "patients")
public class Patient extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "abha_id", length = 14, nullable = false, unique = true)
//    @Pattern(regexp = ApiConstant.Regexp.CONTACT_NUMBER)
    private String abhaNumber;

    @Column(name = "patient_name", length = 200)
    private String patientName;

    @Column(name = "date_of_birth")
    @JsonFormat(pattern = ApiConstant.DateTime.DATE_DD_MM_YYYY)
    private LocalDate patientDob;

    @Column(name = "age")
    private String age;

    @Column(name = "phone", length = 20)
    private String patientMob;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 20)
    private Gender gender;

    @Column(name = "relation", length = 50)
    private String relation;

    @Column(name = "patient_mr_number", length = 50)
    private String patientMrNumber;

    @Column(name = "is_comm_address_same_as_card_address")
    private Boolean isCommAddressSameAsCardAddress;

    @Column(name = "is_child_registration")
    private Boolean isChildRegistration;

    @Embedded
    @EmbeddedColumnNaming("patient_address_%s")
    private Address patientAddress;

    @Embedded
    @EmbeddedColumnNaming("patient_comm_address_%s")
    private Address patientCommAddress;

    @Embedded
    @EmbeddedColumnNaming("contact_person_%s")
    private ContactPerson contactPerson;

    @Embedded
    @EmbeddedColumnNaming("subscriber_%s")
    private Subscriber subscriber;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

/**

    @Column(name = "is_patient_verified")
    private Boolean isPatientVerified;

    @Column(name = "stage", length = 50)
    private String stage;

    @Column(name = "is_subscriber_verified")
    private Boolean isSubscriberVerified;

    @Column(name = "eligibility_id")
    private Long eligibilityId;

    @Column(name = "claim_id")
    private Long claimId;

    @Column(name = "allowed_amount", precision = 15, scale = 2)
    private BigDecimal allowedAmount;

    @Column(name = "used_amount", precision = 15, scale = 2)
    private BigDecimal usedAmount;

    @Column(name = "insured_amount", precision = 15, scale = 2)
    private BigDecimal insuredAmount;


    @Column(name = "registration_bio_auth")
    private Boolean registrationBioAuth;

    @Column(name = "discharge_bio_auth")
    private Boolean dischargeBioAuth;

    @Column(name = "registration_face_auth")
    private Boolean registrationFaceAuth;

    @Column(name = "discharge_face_auth")
    private Boolean dischargeFaceAuth;


    @Column(name = "registration_iris_auth")
    private Boolean registrationIrisAuth;

    @Column(name = "discharge_iris_auth")
    private Boolean dischargeIrisAuth;
*/


}