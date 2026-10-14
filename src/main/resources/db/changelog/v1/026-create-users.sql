--liquibase formatted sql

--changeset carepoint:026

CREATE TABLE users
(
    user_id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    staff_id      BIGINT UNIQUE REFERENCES staff (staff_id),
    username      VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    active       BOOLEAN NOT NULL DEFAULT true,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_active ON users (active);
-- Note: staff_id unique index (from UNIQUE constraint) enforces 1:0..1

CREATE TRIGGER trg_users_updated_at
    BEFORE UPDATE
    ON users
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

--rollback DROP TABLE users;