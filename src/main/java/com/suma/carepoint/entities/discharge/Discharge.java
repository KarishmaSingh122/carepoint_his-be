package com.suma.carepoint.entities.discharge;

import com.suma.carepoint.entities.admission.Admission;
import com.suma.carepoint.entities.organization.Staff;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
@Entity
@Table(name = "discharges")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Discharge {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discharge_id")
    private Long dischargeId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admission_id", nullable = false, unique = true)
    private Admission admission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Staff doctor;

    @Column(name = "discharge_date", nullable = false)
    private OffsetDateTime dischargeDate;

    @Column(name = "discharge_type", nullable = false)
    private String dischargeType;

    @Column(name = "discharge_summary")
    private String dischargeSummary;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}

