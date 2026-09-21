--liquibase formatted sql

--changeset carepoint:009
CREATE TABLE discharges
(
    discharge_id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    admission_id      BIGINT      NOT NULL UNIQUE REFERENCES admissions (admission_id),
    doctor_id         BIGINT REFERENCES staff (staff_id),
    discharge_date    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    discharge_type    VARCHAR(30) NOT NULL CONSTRAINT chk_discharge_type CHECK (
        discharge_type IN ('ROUTINE', 'LAMA','DAMA', 'TRANSFER', 'DEATH', 'ABSCONDED')
        ),
    discharge_summary TEXT,
    created_at        TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_discharges_doctor_id ON discharges (doctor_id);
CREATE INDEX idx_discharges_date ON discharges (discharge_date);
-- Note: admission_id unique index (from UNIQUE constraint) enforces 1:0..1


--rollback DROP TABLE discharges;