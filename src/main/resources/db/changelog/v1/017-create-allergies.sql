--liquibase formatted sql

--changeset carepoint:017
CREATE TABLE allergies
(
    allergy_id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    patient_id   BIGINT       NOT NULL REFERENCES patients (patient_id),
    allergy_name VARCHAR(150) NOT NULL,
    reaction     VARCHAR(255),
    severity     VARCHAR(30),
    active       BOOLEAN NOT NULL DEFAULT true,
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_allergies_patient_id ON allergies (patient_id);
CREATE INDEX idx_allergies_active ON allergies (active);
CREATE INDEX idx_allergies_name ON allergies (allergy_name);


--rollback DROP TABLE allergies;