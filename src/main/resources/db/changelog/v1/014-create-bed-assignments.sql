--liquibase formatted sql

--changeset carepoint:014

CREATE TABLE bed_assignments
(
    assignment_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    bed_id        BIGINT      NOT NULL REFERENCES beds (bed_id),
    patient_id    BIGINT      NOT NULL REFERENCES patients (patient_id),
    admission_id  BIGINT      NOT NULL REFERENCES admissions (admission_id),
    assigned_at   TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    released_at   TIMESTAMPTZ,
    active       BOOLEAN NOT NULL DEFAULT true,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_bed_assignments_bed_id ON bed_assignments (bed_id);
CREATE INDEX idx_bed_assignments_patient_id ON bed_assignments (patient_id);
CREATE INDEX idx_bed_assignments_admission_id ON bed_assignments (admission_id);
CREATE INDEX idx_bed_assignments_active ON bed_assignments (active);

-- Fast lookup: "which bed is currently occupied?"
-- CREATE INDEX idx_bed_assignments_active_bed ON bed_assignments (bed_id) WHERE status = 'ACTIVE' AND released_at IS NULL;

--rollback DROP TABLE bed_assignments;