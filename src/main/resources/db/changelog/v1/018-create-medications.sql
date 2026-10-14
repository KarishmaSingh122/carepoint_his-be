--liquibase formatted sql

--changeset carepoint:018

CREATE TABLE medications
(
    medication_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name          VARCHAR(150) NOT NULL UNIQUE,
    generic_name  VARCHAR(150),
    strength      VARCHAR(50),
    active       BOOLEAN NOT NULL DEFAULT true,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_medications_generic_name ON medications (generic_name);
CREATE INDEX idx_medications_active ON medications (active);

CREATE TRIGGER trg_medications_updated_at
    BEFORE UPDATE
    ON medications
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

--rollback DROP TABLE medications;