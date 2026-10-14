package com.suma.carepoint.entities.allergy;

import com.suma.carepoint.entities.patient.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "allergies")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Allergy {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "allergy_id")
    private Long allergyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "allergy_name", nullable = false, length = 150)
    private String allergyName;

    @Column(name = "reaction", length = 255)
    private String reaction;

    @Column(name = "severity", length = 30)
    private String severity;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}

