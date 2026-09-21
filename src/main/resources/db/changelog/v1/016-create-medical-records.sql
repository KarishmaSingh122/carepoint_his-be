--liquibase formatted sql

--changeset carepoint:016

CREATE TABLE medical_records
(
    medical_record_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    patient_id        BIGINT      NOT NULL REFERENCES patients (patient_id),
    visit_id          BIGINT REFERENCES visits (visit_id),
    doctor_id         BIGINT REFERENCES staff (staff_id),
    diagnosis_id      BIGINT REFERENCES diagnoses (diagnosis_id),
    record_date       TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    symptoms          TEXT,
    treatment         TEXT,
    notes             TEXT,
    created_at        TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_medical_records_patient_id ON medical_records (patient_id);
CREATE INDEX idx_medical_records_visit_id ON medical_records (visit_id);
CREATE INDEX idx_medical_records_doctor_id ON medical_records (doctor_id);
CREATE INDEX idx_medical_records_date ON medical_records (record_date);

CREATE TRIGGER trg_medical_records_updated_at
    BEFORE UPDATE
    ON medical_records
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

--rollback DROP TABLE medical_records;