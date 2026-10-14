

--liquibase formatted sql

--changeset carepoint:012

ALTER TABLE admissions
DROP CONSTRAINT chk_status;

ALTER TABLE admissions
    ADD CONSTRAINT chk_status CHECK (
        status IN ('ADMITTED', 'DISCHARGED', 'TRANSFERRED')
        );