-- SUPER_ADMIN (120 permissions)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM roles r
         JOIN permissions p ON p.permission_name IN (
                                                     'ADMISSION_CREATE',
                                                     'ADMISSION_DELETE',
                                                     'ADMISSION_READ',
                                                     'ADMISSION_UPDATE',
                                                     'ALLERGY_CREATE',
                                                     'ALLERGY_DELETE',
                                                     'ALLERGY_READ',
                                                     'ALLERGY_UPDATE',
                                                     'AUDIT_LOG_READ',
                                                     'BED_ASSIGNMENT_CREATE',
                                                     'BED_ASSIGNMENT_DELETE',
                                                     'BED_ASSIGNMENT_READ',
                                                     'BED_ASSIGNMENT_UPDATE',
                                                     'BED_CREATE',
                                                     'BED_DELETE',
                                                     'BED_READ',
                                                     'BED_UPDATE',
                                                     'BILL_CREATE',
                                                     'BILL_DELETE',
                                                     'BILL_ITEM_CREATE',
                                                     'BILL_ITEM_DELETE',
                                                     'BILL_ITEM_READ',
                                                     'BILL_ITEM_UPDATE',
                                                     'BILL_READ',
                                                     'BILL_UPDATE',
                                                     'DEPARTMENT_CREATE',
                                                     'DEPARTMENT_DELETE',
                                                     'DEPARTMENT_READ',
                                                     'DEPARTMENT_UPDATE',
                                                     'DIAGNOSIS_CREATE',
                                                     'DIAGNOSIS_DELETE',
                                                     'DIAGNOSIS_READ',
                                                     'DIAGNOSIS_UPDATE',
                                                     'DISCHARGE_CREATE',
                                                     'DISCHARGE_DELETE',
                                                     'DISCHARGE_READ',
                                                     'DISCHARGE_UPDATE',
                                                     'DOCUMENT_CREATE',
                                                     'DOCUMENT_DELETE',
                                                     'DOCUMENT_READ',
                                                     'DOCUMENT_UPDATE',
                                                     'FLOOR_CREATE',
                                                     'FLOOR_DELETE',
                                                     'FLOOR_READ',
                                                     'FLOOR_UPDATE',
                                                     'MEDICAL_RECORD_CREATE',
                                                     'MEDICAL_RECORD_DELETE',
                                                     'MEDICAL_RECORD_READ',
                                                     'MEDICAL_RECORD_UPDATE',
                                                     'MEDICATION_CREATE',
                                                     'MEDICATION_DELETE',
                                                     'MEDICATION_READ',
                                                     'MEDICATION_UPDATE',
                                                     'PATIENT_CREATE',
                                                     'PATIENT_DELETE',
                                                     'PATIENT_READ',
                                                     'PATIENT_TREATMENT_CREATE',
                                                     'PATIENT_TREATMENT_DELETE',
                                                     'PATIENT_TREATMENT_READ',
                                                     'PATIENT_TREATMENT_UPDATE',
                                                     'PATIENT_UPDATE',
                                                     'PAYMENT_CREATE',
                                                     'PAYMENT_DELETE',
                                                     'PAYMENT_READ',
                                                     'PAYMENT_UPDATE',
                                                     'PERMISSION_CREATE',
                                                     'PERMISSION_DELETE',
                                                     'PERMISSION_READ',
                                                     'PERMISSION_UPDATE',
                                                     'PRESCRIPTION_CREATE',
                                                     'PRESCRIPTION_DELETE',
                                                     'PRESCRIPTION_ITEM_CREATE',
                                                     'PRESCRIPTION_ITEM_DELETE',
                                                     'PRESCRIPTION_ITEM_READ',
                                                     'PRESCRIPTION_ITEM_UPDATE',
                                                     'PRESCRIPTION_READ',
                                                     'PRESCRIPTION_UPDATE',
                                                     'PROCEDURE_CREATE',
                                                     'PROCEDURE_DELETE',
                                                     'PROCEDURE_READ',
                                                     'PROCEDURE_UPDATE',
                                                     'REFRESH_TOKEN_REVOKE',
                                                     'ROLE_CREATE',
                                                     'ROLE_DELETE',
                                                     'ROLE_PERMISSION_ASSIGN',
                                                     'ROLE_PERMISSION_READ',
                                                     'ROLE_PERMISSION_REVOKE',
                                                     'ROLE_READ',
                                                     'ROLE_UPDATE',
                                                     'ROOM_CREATE',
                                                     'ROOM_DELETE',
                                                     'ROOM_READ',
                                                     'ROOM_UPDATE',
                                                     'SERVICE_CATEGORY_CREATE',
                                                     'SERVICE_CATEGORY_DELETE',
                                                     'SERVICE_CATEGORY_READ',
                                                     'SERVICE_CATEGORY_UPDATE',
                                                     'SERVICE_CREATE',
                                                     'SERVICE_DELETE',
                                                     'SERVICE_READ',
                                                     'SERVICE_UPDATE',
                                                     'STAFF_CREATE',
                                                     'STAFF_DELETE',
                                                     'STAFF_READ',
                                                     'STAFF_UPDATE',
                                                     'USER_CREATE',
                                                     'USER_DELETE',
                                                     'USER_READ',
                                                     'USER_ROLE_ASSIGN',
                                                     'USER_ROLE_READ',
                                                     'USER_ROLE_REVOKE',
                                                     'USER_UPDATE',
                                                     'VISIT_CREATE',
                                                     'VISIT_DELETE',
                                                     'VISIT_READ',
                                                     'VISIT_UPDATE',
                                                     'WARD_CREATE',
                                                     'WARD_DELETE',
                                                     'WARD_READ',
                                                     'WARD_UPDATE'
    )
WHERE r.role_name = 'SUPER_ADMIN'
    ON CONFLICT DO NOTHING;

-- HOSPITAL_ADMIN (61 permissions)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM roles r
         JOIN permissions p ON p.permission_name IN (
                                                     'ADMISSION_READ',
                                                     'ALLERGY_READ',
                                                     'AUDIT_LOG_READ',
                                                     'BED_ASSIGNMENT_READ',
                                                     'BED_CREATE',
                                                     'BED_DELETE',
                                                     'BED_READ',
                                                     'BED_UPDATE',
                                                     'BILL_ITEM_READ',
                                                     'BILL_READ',
                                                     'DEPARTMENT_CREATE',
                                                     'DEPARTMENT_DELETE',
                                                     'DEPARTMENT_READ',
                                                     'DEPARTMENT_UPDATE',
                                                     'DIAGNOSIS_READ',
                                                     'DISCHARGE_READ',
                                                     'DOCUMENT_READ',
                                                     'FLOOR_CREATE',
                                                     'FLOOR_DELETE',
                                                     'FLOOR_READ',
                                                     'FLOOR_UPDATE',
                                                     'MEDICAL_RECORD_READ',
                                                     'MEDICATION_READ',
                                                     'PATIENT_READ',
                                                     'PATIENT_TREATMENT_READ',
                                                     'PAYMENT_READ',
                                                     'PERMISSION_READ',
                                                     'PRESCRIPTION_ITEM_READ',
                                                     'PRESCRIPTION_READ',
                                                     'PROCEDURE_READ',
                                                     'REFRESH_TOKEN_REVOKE',
                                                     'ROLE_PERMISSION_READ',
                                                     'ROLE_READ',
                                                     'ROOM_CREATE',
                                                     'ROOM_DELETE',
                                                     'ROOM_READ',
                                                     'ROOM_UPDATE',
                                                     'SERVICE_CATEGORY_CREATE',
                                                     'SERVICE_CATEGORY_DELETE',
                                                     'SERVICE_CATEGORY_READ',
                                                     'SERVICE_CATEGORY_UPDATE',
                                                     'SERVICE_CREATE',
                                                     'SERVICE_DELETE',
                                                     'SERVICE_READ',
                                                     'SERVICE_UPDATE',
                                                     'STAFF_CREATE',
                                                     'STAFF_DELETE',
                                                     'STAFF_READ',
                                                     'STAFF_UPDATE',
                                                     'USER_CREATE',
                                                     'USER_DELETE',
                                                     'USER_READ',
                                                     'USER_ROLE_ASSIGN',
                                                     'USER_ROLE_READ',
                                                     'USER_ROLE_REVOKE',
                                                     'USER_UPDATE',
                                                     'VISIT_READ',
                                                     'WARD_CREATE',
                                                     'WARD_DELETE',
                                                     'WARD_READ',
                                                     'WARD_UPDATE'
    )
WHERE r.role_name = 'HOSPITAL_ADMIN'
    ON CONFLICT DO NOTHING;

-- DOCTOR (41 permissions)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM roles r
         JOIN permissions p ON p.permission_name IN (
                                                     'ADMISSION_CREATE',
                                                     'ADMISSION_READ',
                                                     'ADMISSION_UPDATE',
                                                     'ALLERGY_CREATE',
                                                     'ALLERGY_READ',
                                                     'ALLERGY_UPDATE',
                                                     'BED_ASSIGNMENT_READ',
                                                     'BED_READ',
                                                     'DEPARTMENT_READ',
                                                     'DIAGNOSIS_CREATE',
                                                     'DIAGNOSIS_READ',
                                                     'DIAGNOSIS_UPDATE',
                                                     'DISCHARGE_CREATE',
                                                     'DISCHARGE_READ',
                                                     'DISCHARGE_UPDATE',
                                                     'DOCUMENT_CREATE',
                                                     'DOCUMENT_READ',
                                                     'DOCUMENT_UPDATE',
                                                     'FLOOR_READ',
                                                     'MEDICAL_RECORD_CREATE',
                                                     'MEDICAL_RECORD_READ',
                                                     'MEDICAL_RECORD_UPDATE',
                                                     'MEDICATION_READ',
                                                     'PATIENT_READ',
                                                     'PATIENT_TREATMENT_CREATE',
                                                     'PATIENT_TREATMENT_READ',
                                                     'PATIENT_TREATMENT_UPDATE',
                                                     'PRESCRIPTION_CREATE',
                                                     'PRESCRIPTION_ITEM_CREATE',
                                                     'PRESCRIPTION_ITEM_READ',
                                                     'PRESCRIPTION_ITEM_UPDATE',
                                                     'PRESCRIPTION_READ',
                                                     'PRESCRIPTION_UPDATE',
                                                     'PROCEDURE_READ',
                                                     'ROOM_READ',
                                                     'SERVICE_READ',
                                                     'STAFF_READ',
                                                     'VISIT_CREATE',
                                                     'VISIT_READ',
                                                     'VISIT_UPDATE',
                                                     'WARD_READ'
    )
WHERE r.role_name = 'DOCTOR'
    ON CONFLICT DO NOTHING;

-- NURSE (27 permissions)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM roles r
         JOIN permissions p ON p.permission_name IN (
                                                     'ADMISSION_READ',
                                                     'ALLERGY_CREATE',
                                                     'ALLERGY_READ',
                                                     'ALLERGY_UPDATE',
                                                     'BED_ASSIGNMENT_CREATE',
                                                     'BED_ASSIGNMENT_READ',
                                                     'BED_ASSIGNMENT_UPDATE',
                                                     'BED_READ',
                                                     'BED_UPDATE',
                                                     'DEPARTMENT_READ',
                                                     'DIAGNOSIS_READ',
                                                     'DISCHARGE_READ',
                                                     'DOCUMENT_CREATE',
                                                     'DOCUMENT_READ',
                                                     'FLOOR_READ',
                                                     'MEDICAL_RECORD_READ',
                                                     'MEDICAL_RECORD_UPDATE',
                                                     'MEDICATION_READ',
                                                     'PATIENT_READ',
                                                     'PATIENT_TREATMENT_READ',
                                                     'PATIENT_TREATMENT_UPDATE',
                                                     'PRESCRIPTION_ITEM_READ',
                                                     'PRESCRIPTION_READ',
                                                     'PROCEDURE_READ',
                                                     'ROOM_READ',
                                                     'VISIT_READ',
                                                     'WARD_READ'
    )
WHERE r.role_name = 'NURSE'
    ON CONFLICT DO NOTHING;

-- RECEPTIONIST (12 permissions)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM roles r
         JOIN permissions p ON p.permission_name IN (
                                                     'ADMISSION_READ',
                                                     'DEPARTMENT_READ',
                                                     'DOCUMENT_CREATE',
                                                     'DOCUMENT_READ',
                                                     'PATIENT_CREATE',
                                                     'PATIENT_READ',
                                                     'PATIENT_UPDATE',
                                                     'SERVICE_READ',
                                                     'STAFF_READ',
                                                     'VISIT_CREATE',
                                                     'VISIT_READ',
                                                     'VISIT_UPDATE'
    )
WHERE r.role_name = 'RECEPTIONIST'
    ON CONFLICT DO NOTHING;

-- PHARMACIST (9 permissions)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM roles r
         JOIN permissions p ON p.permission_name IN (
                                                     'ALLERGY_READ',
                                                     'DIAGNOSIS_READ',
                                                     'MEDICATION_READ',
                                                     'MEDICATION_UPDATE',
                                                     'PATIENT_READ',
                                                     'PRESCRIPTION_ITEM_READ',
                                                     'PRESCRIPTION_ITEM_UPDATE',
                                                     'PRESCRIPTION_READ',
                                                     'PRESCRIPTION_UPDATE'
    )
WHERE r.role_name = 'PHARMACIST'
    ON CONFLICT DO NOTHING;

-- LAB_TECHNICIAN (10 permissions)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM roles r
         JOIN permissions p ON p.permission_name IN (
                                                     'DIAGNOSIS_READ',
                                                     'DOCUMENT_CREATE',
                                                     'DOCUMENT_READ',
                                                     'DOCUMENT_UPDATE',
                                                     'MEDICAL_RECORD_READ',
                                                     'PATIENT_READ',
                                                     'PATIENT_TREATMENT_READ',
                                                     'PROCEDURE_READ',
                                                     'SERVICE_READ',
                                                     'VISIT_READ'
    )
WHERE r.role_name = 'LAB_TECHNICIAN'
    ON CONFLICT DO NOTHING;

-- BILLING_EXECUTIVE (19 permissions)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM roles r
         JOIN permissions p ON p.permission_name IN (
                                                     'ADMISSION_READ',
                                                     'BILL_CREATE',
                                                     'BILL_ITEM_CREATE',
                                                     'BILL_ITEM_DELETE',
                                                     'BILL_ITEM_READ',
                                                     'BILL_ITEM_UPDATE',
                                                     'BILL_READ',
                                                     'BILL_UPDATE',
                                                     'DISCHARGE_READ',
                                                     'MEDICATION_READ',
                                                     'PATIENT_READ',
                                                     'PATIENT_TREATMENT_READ',
                                                     'PAYMENT_CREATE',
                                                     'PAYMENT_READ',
                                                     'PAYMENT_UPDATE',
                                                     'PROCEDURE_READ',
                                                     'SERVICE_CATEGORY_READ',
                                                     'SERVICE_READ',
                                                     'VISIT_READ'
    )
WHERE r.role_name = 'BILLING_EXECUTIVE'
    ON CONFLICT DO NOTHING;

-- ADMISSION_CLERK (20 permissions)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM roles r
         JOIN permissions p ON p.permission_name IN (
                                                     'ADMISSION_CREATE',
                                                     'ADMISSION_READ',
                                                     'ADMISSION_UPDATE',
                                                     'BED_ASSIGNMENT_CREATE',
                                                     'BED_ASSIGNMENT_READ',
                                                     'BED_ASSIGNMENT_UPDATE',
                                                     'BED_READ',
                                                     'BED_UPDATE',
                                                     'DEPARTMENT_READ',
                                                     'DISCHARGE_CREATE',
                                                     'DISCHARGE_READ',
                                                     'DISCHARGE_UPDATE',
                                                     'DOCUMENT_CREATE',
                                                     'DOCUMENT_READ',
                                                     'FLOOR_READ',
                                                     'PATIENT_READ',
                                                     'ROOM_READ',
                                                     'STAFF_READ',
                                                     'VISIT_READ',
                                                     'WARD_READ'
    )
WHERE r.role_name = 'ADMISSION_CLERK'
    ON CONFLICT DO NOTHING;

-- INVENTORY_MANAGER (7 permissions)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM roles r
         JOIN permissions p ON p.permission_name IN (
                                                     'MEDICATION_CREATE',
                                                     'MEDICATION_DELETE',
                                                     'MEDICATION_READ',
                                                     'MEDICATION_UPDATE',
                                                     'PRESCRIPTION_ITEM_READ',
                                                     'PRESCRIPTION_READ',
                                                     'SERVICE_READ'
    )
WHERE r.role_name = 'INVENTORY_MANAGER'
    ON CONFLICT DO NOTHING;