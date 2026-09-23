package com.suma.carepoint.entities.visit;


import com.suma.carepoint.entities.base.BaseEntity;
import com.suma.carepoint.entities.organization.Department;
import com.suma.carepoint.entities.organization.Staff;
import com.suma.carepoint.entities.patient.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

import static com.suma.carepoint.entities.visit.VisitStatus.OPEN;

@Entity
@Table(name = "visits")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Visit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visit_id")
    private Long visitId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Staff doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Enumerated(EnumType.STRING)
    @Column(name = "visit_type", nullable = false)
    private VisitType visitType;

    @Column(name = "visit_date", nullable = false)
    private Instant visitDate;

    @Column(name = "reason")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VisitStatus status;


    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        if (visitDate == null) {
            visitDate = now;
        }
        if (status == null) {
            status = OPEN;
        }
    }

}
