package com.suma.carepoint.entities.emr;

import com.suma.carepoint.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "diagnoses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diagnosis extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diagnosis_id")
    private Long diagnosisId;

    @Column(name = "diagnosis_code", nullable = false, unique = true, length = 30)
    private String diagnosisCode;

    @Column(name = "diagnosis_name", nullable = false, unique = true, length = 200)
    private String diagnosisName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;
}