--liquibase formatted sql

--changeset carepoint:021

CREATE TABLE service_categories
(
    service_category_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    category_name       VARCHAR(100) NOT NULL UNIQUE,
    description         TEXT,
    active       BOOLEAN NOT NULL DEFAULT true
);

CREATE INDEX idx_service_categories_active ON service_categories (active);


--rollback DROP TABLE service_categories;