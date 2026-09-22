--liquibase formatted sql

--changeset carepoint:030

CREATE TABLE role_permissions
(
    role_id       BIGINT NOT NULL REFERENCES roles (role_id) ON DELETE CASCADE,
    permission_id BIGINT NOT NULL REFERENCES permissions (permission_id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

CREATE INDEX idx_role_permissions_permission_id ON role_permissions (permission_id);
-- Note: role_id index exists via composite PK (role_id, permission_id)

--rollback DROP TABLE role_permissions;