--liquibase formatted sql

--changeset carepoint:011
CREATE TABLE wards
(
    ward_id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    department_id BIGINT       NOT NULL REFERENCES departments (department_id),
    floor_id      BIGINT       NOT NULL REFERENCES floors (floor_id),
    ward_name     VARCHAR(100) NOT NULL,
    ward_type     VARCHAR(50)  NOT NULL CONSTRAINT chk_ward_type CHECK (
        ward_type IN (
                      'GENERAL', 'ICU', 'NICU', 'HDU', 'MATERNITY',
                      'PEDIATRIC', 'ISOLATION', 'PRIVATE', 'SEMI_PRIVATE'
            )
        ),
    active       BOOLEAN NOT NULL DEFAULT true,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_wards_dept_name UNIQUE (department_id, ward_name)
);

CREATE INDEX idx_wards_department_id ON wards (department_id);
CREATE INDEX idx_wards_active ON wards (active);

CREATE TRIGGER trg_wards_updated_at
    BEFORE UPDATE
    ON wards
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();


--rollback DROP TABLE wards;