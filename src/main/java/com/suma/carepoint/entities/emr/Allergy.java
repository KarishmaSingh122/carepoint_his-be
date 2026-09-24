package com.suma.carepoint.entities.emr;

import com.suma.carepoint.entities.patient.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "allergies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Allergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "allergy_id")
    private Long allergyId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "allergy_name", nullable = false, length = 150)
    private String allergyName;

    @Column(name = "reaction", length = 255)
    private String reaction;

    @Column(name = "severity", length = 30)
    private String severity;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    // Your database currently has only created_at.
    // Add @CreatedDate here only if you are using JPA auditing
    // and want this entity to automatically populate createdAt.
}