package com.suma.carepoint.entities.medication;

import com.suma.carepoint.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "prescription_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prescription_item_id")
    private Long prescriptionItemId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "prescription_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_prescription_item_prescription")
    )
    private Prescription prescription;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "medication_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_prescription_item_medication")
    )
    private Medication medication;

    @Column(name = "dosage", length = 100)
    private String dosage;

    @Column(name = "frequency", length = 100)
    private String frequency;

    @Column(name = "duration", length = 100)
    private String duration;

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

//    @Column(name = "created_at", nullable = false, updatable = false)
//    private java.time.OffsetDateTime createdAt;
}
