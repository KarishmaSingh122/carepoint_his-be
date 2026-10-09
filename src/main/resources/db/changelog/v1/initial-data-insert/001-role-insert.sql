INSERT INTO roles (role_name, description, active)
VALUES
    ('SUPER_ADMIN', 'Full system access and configuration.', TRUE),
    ('HOSPITAL_ADMIN', 'Manages hospital operations, users, and master data.', TRUE),
    ('DOCTOR', 'Manages consultations, diagnoses, prescriptions, and treatment plans.', TRUE),
    ('NURSE', 'Manages vital signs, nursing assessments, and patient care.', TRUE),
    ('RECEPTIONIST', 'Manages patient registration and appointments.', TRUE),
    ('PHARMACIST', 'Manages medications, prescriptions, and dispensing.', TRUE),
    ('LAB_TECHNICIAN', 'Manages laboratory tests, specimens, and results.', TRUE),
    ('BILLING_EXECUTIVE', 'Manages patient bills, charges, and payments.', TRUE),
    ('ADMISSION_CLERK', 'Manages inpatient admissions, transfers, and discharge.', TRUE),
    ('INVENTORY_MANAGER', 'Manages medicine and hospital inventory.', TRUE)
ON CONFLICT (role_name) DO NOTHING;