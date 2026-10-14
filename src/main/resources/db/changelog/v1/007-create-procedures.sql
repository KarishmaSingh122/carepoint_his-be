--liquibase formatted sql

--changeset carepoint:007

CREATE TABLE procedures
(
    procedure_id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    procedure_code VARCHAR(30)  NOT NULL UNIQUE,
    procedure_name VARCHAR(200) NOT NULL UNIQUE,
    description    TEXT,
    active       BOOLEAN NOT NULL DEFAULT true,
    created_at     TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP

--     CONSTRAINT chk_procedures_status CHECK (status IN ('ACTIVE', 'INACTIVE'))

);
CREATE INDEX idx_procedures_name ON procedures (procedure_name);
CREATE INDEX idx_procedures_active ON procedures (active);
CREATE INDEX idx_procedures_code ON procedures (procedure_code);
CREATE TRIGGER trg_procedures_updated_at
    BEFORE UPDATE
    ON procedures
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();


--rollback DROP TABLE procedures;