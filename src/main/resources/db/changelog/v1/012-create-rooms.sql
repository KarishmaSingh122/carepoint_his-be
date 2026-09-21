--liquibase formatted sql

--changeset carepoint:012
CREATE TABLE rooms
(
    room_id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ward_id     BIGINT      NOT NULL REFERENCES wards (ward_id),
    room_number VARCHAR(20) NOT NULL,
    room_type   VARCHAR(50) NOT NULL CONSTRAINT chk_room_type CHECK (
        room_type IN ('SINGLE', 'TWIN_SHARING', 'GENERAL_WARD_ROOM', 'SUITE', 'ISOLATION_ROOM', 'TREATMENT_ROOM')
        ),
    status      VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    created_at  TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_rooms_ward_roomno UNIQUE (ward_id, room_number)
);

CREATE INDEX idx_rooms_ward_id ON rooms (ward_id);
CREATE INDEX idx_rooms_status ON rooms (status);

CREATE TRIGGER trg_rooms_updated_at
    BEFORE UPDATE
    ON rooms
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();


--rollback DROP TABLE rooms;