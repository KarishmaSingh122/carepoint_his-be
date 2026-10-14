--liquibase formatted sql

--changeset carepoint:013
CREATE TABLE beds
(
    bed_id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    room_id    BIGINT      NOT NULL REFERENCES rooms (room_id),
    bed_number VARCHAR(20) NOT NULL,
    status     VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_beds_room_bedno UNIQUE (room_id, bed_number)
);

CREATE INDEX idx_beds_room_id ON beds (room_id);
CREATE INDEX idx_beds_status ON beds (status);

CREATE TRIGGER trg_beds_updated_at
    BEFORE UPDATE
    ON beds
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();


--rollback DROP TABLE beds;