--liquibase formatted sql

--changeset carepoint:032

CREATE TABLE audit_logs
(
    audit_id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id    BIGINT REFERENCES users (user_id),
    table_name VARCHAR(100) NOT NULL,
    record_id  BIGINT,
    action     VARCHAR(30)  NOT NULL,
    old_value  JSONB,
    new_value  JSONB,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_logs_user_id ON audit_logs (user_id);
CREATE INDEX idx_audit_logs_table_name ON audit_logs (table_name);
CREATE INDEX idx_audit_logs_record_id ON audit_logs (table_name, record_id);
CREATE INDEX idx_audit_logs_created_at ON audit_logs (created_at);
CREATE INDEX idx_audit_logs_old_values ON audit_logs USING GIN (old_value);
CREATE INDEX idx_audit_logs_new_values ON audit_logs USING GIN (new_value);

--rollback DROP TABLE audit_logs;