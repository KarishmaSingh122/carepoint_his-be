package com.suma.carepoint.entities.medication;

import com.suma.carepoint.entities.base.BaseEntity;
import com.suma.carepoint.entities.patient.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prescriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prescription_id")
    private Long prescriptionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "patient_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_prescription_patient")
    )
    private Patient patient;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(
//            name = "visit_id",
//            foreignKey = @ForeignKey(name = "fk_prescription_visit")
//    )
//    private Visit visit;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(
//            name = "doctor_id",
//            foreignKey = @ForeignKey(name = "fk_prescription_doctor")
//    )
//    private Staff doctor;

    @Column(name = "prescription_date", nullable = false)
    private OffsetDateTime prescriptionDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

//    @Column(name = "created_at", nullable = false, updatable = false)
//    private OffsetDateTime createdAt;

    @OneToMany(
            mappedBy = "prescription",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<PrescriptionItem> items = new ArrayList<>();
}
