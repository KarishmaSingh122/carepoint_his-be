--liquibase formatted sql

--changeset carepoint:004

CREATE TABLE staff
(
    staff_id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    employee_no    VARCHAR(30)  NOT NULL UNIQUE,
    first_name     VARCHAR(100) NOT NULL,
    last_name      VARCHAR(100) NOT NULL,
    phone          VARCHAR(20),
    email          VARCHAR(150) NULL UNIQUE,
    designation    VARCHAR(100),
    specialization VARCHAR(100),
    department_id  BIGINT       NOT NULL REFERENCES departments (department_id),
    joining_date   DATE,
    active         BOOLEAN      NOT NULL DEFAULT true,
    created_at     TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_staff_department_id ON staff (department_id);

CREATE INDEX idx_staff_active ON staff (active);

CREATE INDEX idx_staff_designation ON staff (designation);

CREATE INDEX idx_staff_name ON staff (last_name, first_name);

CREATE TRIGGER trg_staff_updated_at
    BEFORE UPDATE
    ON staff
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

--rollback DROP TABLE staff;

