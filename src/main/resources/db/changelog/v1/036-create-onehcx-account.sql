--liquibase formatted sql

--changeset carepoint:005

CREATE TABLE onehcx_account
(
    id                   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username             VARCHAR(150) NOT NULL,
    password             TEXT,
    token_type           VARCHAR(30),
    token_value          TEXT,
    token_expiry         TIMESTAMPTZ,
    auth_status          VARCHAR(20) NOT NULL DEFAULT 'LOGGED_OUT',
    last_login           TIMESTAMPTZ,
    last_password_change TIMESTAMPTZ,
    last_token_refresh   TIMESTAMPTZ,
    last_error           VARCHAR(1000),
    staff_id             BIGINT,

    CONSTRAINT uk_onehcx_account_username UNIQUE (username),
    CONSTRAINT fk_onehcx_account_staff
        FOREIGN KEY (staff_id) REFERENCES staff(staff_id)
);

CREATE INDEX idx_onehcx_account_token_expiry
    ON onehcx_account (token_expiry);

--rollback DROP TABLE onehcx_account;