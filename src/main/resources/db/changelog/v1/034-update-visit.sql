

ALTER TABLE visits
    ADD COLUMN diagnosis_id BIGINT;

ALTER TABLE visits
    ADD CONSTRAINT fk_visits_diagnosis
        FOREIGN KEY (diagnosis_id)
            REFERENCES diagnoses(diagnosis_id);