--liquibase formatted sql

--changeset carepoint:002

CREATE TABLE patients
(
    patient_id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    abha_id                 VARCHAR(30)  NOT NULL UNIQUE,
    first_name              VARCHAR(100) NOT NULL,
    last_name               VARCHAR(100) NOT NULL,
    date_of_birth           DATE,
    gender                  VARCHAR(20),
    blood_group             VARCHAR(10),
    phone                   VARCHAR(20),
    email                   VARCHAR(150) NULL UNIQUE,
    address                 TEXT,
    city                    VARCHAR(100),
    state                   VARCHAR(100),
    pincode                 VARCHAR(10),
    emergency_contact_name  VARCHAR(150),
    emergency_contact_phone VARCHAR(20),
    active                  BOOLEAN      NOT NULL DEFAULT true,
--     last_visited_at         TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at              TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_patients_gender CHECK (gender IN ('MALE', 'FEMALE', 'OTHER')),

    CONSTRAINT chk_patients_blood_group CHECK ( blood_group IS NULL OR
                                                blood_group IN ('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-') )
);

CREATE INDEX idx_patients_name ON patients (last_name, first_name);

CREATE INDEX idx_patients_phone ON patients (phone);

CREATE INDEX idx_patients_city ON patients (city);

CREATE INDEX idx_patients_active ON patients (active);

CREATE INDEX idx_patients_dob ON patients (date_of_birth);

CREATE TRIGGER trg_patients_updated_at
    BEFORE UPDATE
    ON patients
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

--rollback DROP TABLE patients;
