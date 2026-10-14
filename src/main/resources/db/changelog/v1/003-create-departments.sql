--liquibase formatted sql

--changeset carepoint:003

CREATE TABLE departments
(
    department_id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    department_code VARCHAR(20)  NOT NULL UNIQUE,
    department_name VARCHAR(100) NOT NULL UNIQUE,
    description     TEXT,
    active          BOOLEAN      NOT NULL DEFAULT true,
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_departments_active ON departments (active);

CREATE TRIGGER trg_departments_updated_at
    BEFORE UPDATE
    ON departments
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

--rollback DROP TABLE departments;
