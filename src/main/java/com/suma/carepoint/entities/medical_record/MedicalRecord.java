package com.suma.carepoint.entities.medical_record;


import com.suma.carepoint.entities.base.BaseEntity;
import com.suma.carepoint.entities.emr.Diagnosis;
import com.suma.carepoint.entities.organization.Staff;
import com.suma.carepoint.entities.patient.Patient;
import com.suma.carepoint.entities.visit.Visit;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "medical_records")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medical_record_id")
    private Long medicalRecordId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id")
    private Visit visit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Staff doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diagnosis_id")
    private Diagnosis diagnosis;

    @Column(name = "record_date", nullable = false)
    private Instant recordDate;

    @Column(name = "symptoms")
    private String symptoms;

    @Column(name = "treatment")
    private String treatment;

    @Column(name = "notes")
    private String notes;

    @PrePersist
    protected void onCreateRecord() {

        if (recordDate == null) {
            recordDate = Instant.now();
        }
    }
}
