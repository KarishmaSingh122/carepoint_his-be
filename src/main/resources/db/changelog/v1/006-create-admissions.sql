--liquibase formatted sql

--changeset carepoint:006

CREATE TABLE admissions
(
    admission_id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    admission_no        VARCHAR(30) NOT NULL UNIQUE,
    patient_id          BIGINT      NOT NULL REFERENCES patients (patient_id),
    admitting_doctor_id BIGINT REFERENCES staff (staff_id),
    admission_date      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    admission_type      VARCHAR(30) NOT NULL CONSTRAINT chk_admission_type CHECK (
        admission_type IN ('ROUTINE', 'EMERGENCY', 'URGENT', 'TRANSFER', 'NEWBORN')
        ),
    reason              TEXT,
    status              VARCHAR(20) NOT NULL DEFAULT 'ADMITTED' CONSTRAINT chk_status CHECK (
        status IN ('ADMITTED', 'DISCHARGE','TRANSFER')
        ),
    created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_admissions_patient_id ON admissions (patient_id);

CREATE INDEX idx_admissions_doctor_id ON admissions (admitting_doctor_id);

CREATE INDEX idx_admissions_admission_date ON admissions (admission_date);

CREATE INDEX idx_admissions_status ON admissions (status);

CREATE TRIGGER trg_admissions_updated_at
    BEFORE UPDATE
    ON admissions
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

--rollback DROP TABLE admissions;
