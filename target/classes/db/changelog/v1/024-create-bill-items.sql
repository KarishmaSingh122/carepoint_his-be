--liquibase formatted sql

--changeset carepoint:024

CREATE TABLE bill_items
(
    bill_item_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    bill_id      BIGINT         NOT NULL REFERENCES bills (bill_id) ON DELETE CASCADE,
    service_id   BIGINT         NOT NULL REFERENCES services (service_id),
    quantity     INT            NOT NULL DEFAULT 1
        CONSTRAINT chk_bill_items_qty CHECK (quantity > 0),
    unit_price   NUMERIC(12, 2) NOT NULL,
    discount     NUMERIC(12, 2) NOT NULL DEFAULT 0,
    amount       NUMERIC(12, 2) NOT NULL,
    created_at   TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP

);

CREATE INDEX idx_bill_items_bill_id ON bill_items (bill_id);
CREATE INDEX idx_bill_items_service_id ON bill_items (service_id);

--rollback DROP TABLE bill_items;