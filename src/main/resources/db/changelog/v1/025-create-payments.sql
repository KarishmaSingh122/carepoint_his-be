--liquibase formatted sql

--changeset carepoint:025

CREATE TABLE payments
(
    payment_id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    bill_id               BIGINT         NOT NULL REFERENCES bills (bill_id),
    payment_date          TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    amount                NUMERIC(12, 2) NOT NULL
        CONSTRAINT chk_payments_amount CHECK (amount > 0),
    payment_method        VARCHAR(30)    NOT NULL CONSTRAINT chk_payment_method CHECK (
        payment_method IN ('CASH', 'CREDIT_CARD', 'DEBIT_CARD', 'UPI', 'INSURANCE', 'BANK_TRANSFER', 'CHEQUE', 'WALLET')
        ),
    transaction_reference VARCHAR(100),
    is_active                VARCHAR(20)    NOT NULL DEFAULT 'SUCCESS',
    created_at            TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_payments_bill_id ON payments (bill_id);
CREATE INDEX idx_payments_date ON payments (payment_date);
CREATE INDEX idx_payments_is_active ON payments (is_active);
CREATE INDEX idx_payments_txn_ref ON payments (transaction_reference);


--rollback DROP TABLE payments;