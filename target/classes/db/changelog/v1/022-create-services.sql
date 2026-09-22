--liquibase formatted sql

--changeset carepoint:022
CREATE TABLE services
(
    service_id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    service_code        VARCHAR(30)  NOT NULL UNIQUE,
    service_name        VARCHAR(150) NOT NULL,
    service_category_id BIGINT REFERENCES service_categories (service_category_id),
    default_price       NUMERIC(12, 2),
    active       BOOLEAN NOT NULL DEFAULT true,
    created_at          TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_services_category_id ON services (service_category_id);
CREATE INDEX idx_services_name ON services (service_name);
CREATE INDEX idx_services_active ON services (active);

CREATE TRIGGER trg_services_updated_at
    BEFORE UPDATE
    ON services
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();


--rollback DROP TABLE services;