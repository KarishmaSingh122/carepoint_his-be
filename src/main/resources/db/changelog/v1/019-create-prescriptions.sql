--liquibase formatted sql

--changeset carepoint:019

CREATE TABLE prescriptions
(
    prescription_id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    patient_id        BIGINT      NOT NULL REFERENCES patients (patient_id),
    visit_id          BIGINT REFERENCES visits (visit_id),
    doctor_id         BIGINT REFERENCES staff (staff_id),
    prescription_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    notes             TEXT,
    active       BOOLEAN NOT NULL DEFAULT true,
    created_at        TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_prescriptions_patient_id ON prescriptions (patient_id);
CREATE INDEX idx_prescriptions_visit_id ON prescriptions (visit_id);
CREATE INDEX idx_prescriptions_doctor_id ON prescriptions (doctor_id);
CREATE INDEX idx_prescriptions_date ON prescriptions (prescription_date);
CREATE INDEX idx_prescriptions_active ON prescriptions (active);


--rollback DROP TABLE prescriptions;