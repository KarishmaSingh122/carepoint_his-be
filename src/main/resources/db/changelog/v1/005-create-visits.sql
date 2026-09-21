--liquibase formatted sql

--changeset carepoint:005

CREATE TABLE visits
(
    visit_id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    patient_id    BIGINT      NOT NULL REFERENCES patients (patient_id),
    doctor_id     BIGINT REFERENCES staff (staff_id),
    department_id BIGINT REFERENCES departments (department_id),
    visit_type    VARCHAR(30) NOT NULL CONSTRAINT chk_visit_type CHECK (
        visit_type IN (
                       'OPD', 'IPD', 'EMERGENCY', 'DAYCARE', 'TELEHEALTH',
                       'NEW_CASE', 'FOLLOW_UP', 'ANCILLARY_NEW', 'ANCILLARY_FOLLOW',
                       'ANC', 'PNC', 'IMMUNIZATION', 'OUTREACH',
                       'MEDICAL_REVIEW', 'DRUG_REFILL', 'COUNSELING'
            )
        ),
    visit_date    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reason        TEXT,
    status        VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    created_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_visits_patient_id ON visits (patient_id);

CREATE INDEX idx_visits_doctor_id ON visits (doctor_id);

CREATE INDEX idx_visits_department_id ON visits (department_id);

CREATE INDEX idx_visits_visit_date ON visits (visit_date);

CREATE INDEX idx_visits_status ON visits (status);

CREATE INDEX idx_visits_patient_date ON visits (patient_id, visit_date);

CREATE TRIGGER trg_visits_updated_at
    BEFORE UPDATE
    ON visits
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();


--rollback DROP TABLE visits;
