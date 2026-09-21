--liquibase formatted sql

--changeset carepoint:015
CREATE TABLE diagnoses
(
    diagnosis_id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    diagnosis_code VARCHAR(30)  NOT NULL UNIQUE,
    diagnosis_name VARCHAR(200) NOT NULL UNIQUE,
    description    TEXT,
    active       BOOLEAN NOT NULL DEFAULT true,
    created_at     TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP

--     CONSTRAINT chk_diagnoses_status CHECK (status IN ('ACTIVE', 'INACTIVE'))

);
CREATE INDEX idx_diagnoses_name ON diagnoses (diagnosis_name);
CREATE INDEX idx_diagnoses_active ON diagnoses (active);
CREATE INDEX idx_diagnoses_code ON diagnoses (diagnosis_code);
CREATE TRIGGER trg_diagnoses_updated_at
    BEFORE UPDATE
    ON diagnoses
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();


--rollback DROP TABLE diagnoses;