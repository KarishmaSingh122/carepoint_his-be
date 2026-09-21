--liquibase formatted sql

--changeset carepoint:029

CREATE TABLE user_roles
(
    user_id     BIGINT      NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    role_id     BIGINT      NOT NULL REFERENCES roles (role_id) ON DELETE CASCADE,
    assigned_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, role_id)
);

CREATE INDEX idx_user_roles_role_id ON user_roles (role_id);
-- Note: user_id index exists via composite PK (user_id, role_id)


--rollback DROP TABLE user_roles;