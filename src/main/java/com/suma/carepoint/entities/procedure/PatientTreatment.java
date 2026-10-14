package com.suma.carepoint.entities.procedure;


import com.suma.carepoint.entities.admission.Admission;
import com.suma.carepoint.entities.base.BaseEntity;
import com.suma.carepoint.entities.organization.Staff;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "patient_treatments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientTreatment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "treatment_id")
    private Long treatmentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "admission_id",
            nullable = false
    )
    private Admission admission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Staff doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procedure_id")
    private Procedure procedure;

    @Column(name = "treatment_date", nullable = false)
    private Instant treatmentDate;

    @Column(name = "description")
    private String description;

    @Column(name = "remarks")
    private String remarks;
}
