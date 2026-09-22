--liquibase formatted sql

--changeset carepoint:020

CREATE TABLE prescription_items
(
    prescription_item_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    prescription_id      BIGINT      NOT NULL REFERENCES prescriptions (prescription_id) ON DELETE CASCADE,
    medication_id        BIGINT      NOT NULL REFERENCES medications (medication_id),
    dosage               VARCHAR(100),
    frequency            VARCHAR(100),
    duration             VARCHAR(100),
    instructions         TEXT,
    created_at           TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_prescription_items_prescription_id ON prescription_items (prescription_id);
CREATE INDEX idx_prescription_items_medication_id ON prescription_items (medication_id);

--rollback DROP TABLE prescription_items;