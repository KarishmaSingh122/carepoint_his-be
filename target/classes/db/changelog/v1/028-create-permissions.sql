--liquibase formatted sql

--changeset carepoint:028

CREATE TABLE permissions
(
    permission_id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    permission_name VARCHAR(100) NOT NULL UNIQUE,
    module_name     VARCHAR(100),
    action          VARCHAR(30),
    description     TEXT
);

CREATE INDEX idx_permissions_module ON permissions (module_name);

--rollback DROP TABLE permissions;