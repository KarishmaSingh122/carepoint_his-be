package com.suma.carepoint.entities.admission;
import com.suma.carepoint.entities.base.BaseEntity;
import com.suma.carepoint.entities.department.Staff;
import com.suma.carepoint.entities.patient.Patient;
import com.suma.carepoint.models.admission.AdmissionStatus;
import com.suma.carepoint.models.admission.AdmissionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="admissions")
public class Admission extends BaseEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long admissionId;

        @Column(name = "admission_no", nullable = false)
        private String admissionNumber;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "patient_id", nullable = false)
        private Patient patient;

        @Column(name = "admission_date", nullable = false)
        private OffsetDateTime admissionDate;

        @Enumerated(EnumType.STRING)
        @Column(name = "admission_type", nullable = false)
        private AdmissionType admissionType;

        @Enumerated(EnumType.STRING)
        @Column(name = "status", nullable = false)
        private AdmissionStatus status;

        @Column(name = "reason")
        private String reason;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "admitting_doctor_id")
        private Staff admittingDoctor;
    }