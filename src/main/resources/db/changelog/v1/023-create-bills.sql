--liquibase formatted sql

--changeset carepoint:023

CREATE TABLE bills
(
    bill_id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    bill_no      VARCHAR(30)    NOT NULL UNIQUE,
    patient_id   BIGINT         NOT NULL REFERENCES patients (patient_id),
    visit_id     BIGINT REFERENCES visits (visit_id),
    admission_id BIGINT REFERENCES admissions (admission_id),
    bill_date    TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    subtotal     NUMERIC(12, 2) NOT NULL DEFAULT 0,
    discount     NUMERIC(12, 2) NOT NULL DEFAULT 0,
    tax          NUMERIC(12, 2) NOT NULL DEFAULT 0,
    total_amount NUMERIC(12, 2) NOT NULL DEFAULT 0,
    status       VARCHAR(20)    NOT NULL DEFAULT 'PENDING',
    created_at   TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP

);

CREATE INDEX idx_bills_patient_id ON bills (patient_id);
CREATE INDEX idx_bills_visit_id ON bills (visit_id);
CREATE INDEX idx_bills_admission_id ON bills (admission_id);
CREATE INDEX idx_bills_bill_date ON bills (bill_date);
CREATE INDEX idx_bills_status ON bills (status);

--rollback DROP TABLE bills;