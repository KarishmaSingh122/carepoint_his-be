--liquibase formatted sql

--changeset carepoint:010


CREATE TABLE floors
(
    floor_id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    floor_number BIGINT       NOT NULL UNIQUE,
    floor_name   VARCHAR(100) NOT NULL,
    description  TEXT,
    active       BOOLEAN      NOT NULL DEFAULT true,
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_floors_active ON floors (active);

CREATE TRIGGER trg_floors_updated_at
    BEFORE UPDATE
    ON floors
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();


--rollback DROP TABLE floors;