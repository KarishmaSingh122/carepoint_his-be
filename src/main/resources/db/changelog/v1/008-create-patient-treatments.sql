--liquibase formatted sql

--changeset carepoint:008
CREATE TABLE patient_treatments
(
    treatment_id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    admission_id   BIGINT      NOT NULL REFERENCES admissions (admission_id),
    doctor_id      BIGINT REFERENCES staff (staff_id),
    procedure_id   BIGINT REFERENCES procedures (procedure_id),
    treatment_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    description    TEXT,
    remarks        TEXT,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP

);

CREATE INDEX idx_treatments_admission_id ON patient_treatments (admission_id);
CREATE INDEX idx_treatments_doctor_id ON patient_treatments (doctor_id);
CREATE INDEX idx_treatments_date ON patient_treatments (treatment_date);


--rollback DROP TABLE patient_treatments;