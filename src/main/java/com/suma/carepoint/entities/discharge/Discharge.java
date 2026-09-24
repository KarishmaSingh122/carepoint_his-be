package com.suma.carepoint.entities.discharge;

import com.suma.carepoint.entities.admission.Admission;
import com.suma.carepoint.entities.organization.Staff;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "discharges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Discharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discharge_id")
    private Long dischargeId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "admission_id",
            nullable = false,
            unique = true
    )
    private Admission admission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Staff doctor;

    @Column(
            name = "discharge_date",
            nullable = false
    )
    @Builder.Default
    private Instant dischargeDate = Instant.now();

    @Enumerated(EnumType.STRING)
    @Column(
            name = "discharge_type",
            nullable = false,
            length = 30
    )
    private DischargeType dischargeType;

    @Column(
            name = "discharge_summary",
            columnDefinition = "TEXT"
    )
    private String dischargeSummary;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    @Builder.Default
    private Instant createdAt = Instant.now();
}