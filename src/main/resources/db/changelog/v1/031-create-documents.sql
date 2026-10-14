--liquibase formatted sql

--changeset carepoint:031

CREATE TABLE documents
(
    document_id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    patient_id       BIGINT        NOT NULL REFERENCES patients (patient_id),
    visit_id         BIGINT REFERENCES visits (visit_id),
    admission_id     BIGINT REFERENCES admissions (admission_id),
    uploaded_by      BIGINT        NOT NULL REFERENCES users (user_id),
    document_type    VARCHAR(50)   NOT NULL,
    file_name        VARCHAR(255)  NOT NULL,
    file_path        VARCHAR(1000) NOT NULL,
    file_size        BIGINT,
    content_type     VARCHAR(255),
    stored_file_name VARCHAR(255)  NOT NULL,
    created_at       TIMESTAMPTZ   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    uploaded_at      TIMESTAMPTZ   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    verified         BOOLEAN NOT NULL DEFAULT true
);

CREATE INDEX idx_documents_patient_id ON documents (patient_id);
CREATE INDEX idx_documents_visit_id ON documents (visit_id);
CREATE INDEX idx_documents_admission_id ON documents (admission_id);
CREATE INDEX idx_documents_uploaded_by ON documents (uploaded_by);
CREATE INDEX idx_documents_type ON documents (document_type);
CREATE INDEX idx_documents_verified ON documents (verified);

--rollback DROP TABLE documents;