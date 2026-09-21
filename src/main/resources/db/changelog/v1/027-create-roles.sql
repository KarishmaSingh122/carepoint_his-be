--liquibase formatted sql

--changeset carepoint:027

CREATE TABLE roles
(
    role_id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    role_name   VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    active       BOOLEAN NOT NULL DEFAULT true
);

CREATE INDEX idx_roles_active ON roles (active);


--rollback DROP TABLE roles;