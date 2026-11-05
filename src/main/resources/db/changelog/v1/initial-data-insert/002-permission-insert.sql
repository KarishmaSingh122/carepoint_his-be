
INSERT INTO permissions (permission_name, module_name, action, description)
VALUES
    ('PATIENT_CREATE', 'PATIENT', 'CREATE', 'Create a patient'),
    ('PATIENT_READ', 'PATIENT', 'READ', 'View patient details'),
    ('PATIENT_UPDATE', 'PATIENT', 'UPDATE', 'Update patient details'),
    ('PATIENT_DELETE', 'PATIENT', 'DELETE', 'Delete a patient'),

    ('DEPARTMENT_CREATE', 'DEPARTMENT', 'CREATE', 'Create a department'),
    ('DEPARTMENT_READ', 'DEPARTMENT', 'READ', 'View departments'),
    ('DEPARTMENT_UPDATE', 'DEPARTMENT', 'UPDATE', 'Update a department'),
    ('DEPARTMENT_DELETE', 'DEPARTMENT', 'DELETE', 'Delete a department'),

    ('STAFF_CREATE', 'STAFF', 'CREATE', 'Create staff'),
    ('STAFF_READ', 'STAFF', 'READ', 'View staff details'),
    ('STAFF_UPDATE', 'STAFF', 'UPDATE', 'Update staff details'),
    ('STAFF_DELETE', 'STAFF', 'DELETE', 'Delete staff'),

    ('VISIT_CREATE', 'VISIT', 'CREATE', 'Create a visit'),
    ('VISIT_READ', 'VISIT', 'READ', 'View visits'),
    ('VISIT_UPDATE', 'VISIT', 'UPDATE', 'Update a visit'),
    ('VISIT_DELETE', 'VISIT', 'DELETE', 'Delete a visit'),

    ('ADMISSION_CREATE', 'ADMISSION', 'CREATE', 'Create an admission'),
    ('ADMISSION_READ', 'ADMISSION', 'READ', 'View admissions'),
    ('ADMISSION_UPDATE', 'ADMISSION', 'UPDATE', 'Update an admission'),
    ('ADMISSION_DELETE', 'ADMISSION', 'DELETE', 'Delete an admission'),

    ('PROCEDURE_CREATE', 'PROCEDURE', 'CREATE', 'Create a procedure'),
    ('PROCEDURE_READ', 'PROCEDURE', 'READ', 'View procedures'),
    ('PROCEDURE_UPDATE', 'PROCEDURE', 'UPDATE', 'Update a procedure'),
    ('PROCEDURE_DELETE', 'PROCEDURE', 'DELETE', 'Delete a procedure'),

    ('PATIENT_TREATMENT_CREATE', 'PATIENT_TREATMENT', 'CREATE', 'Create patient treatment'),
    ('PATIENT_TREATMENT_READ', 'PATIENT_TREATMENT', 'READ', 'View patient treatments'),
    ('PATIENT_TREATMENT_UPDATE', 'PATIENT_TREATMENT', 'UPDATE', 'Update patient treatment'),
    ('PATIENT_TREATMENT_DELETE', 'PATIENT_TREATMENT', 'DELETE', 'Delete patient treatment'),

    ('DISCHARGE_CREATE', 'DISCHARGE', 'CREATE', 'Create a discharge'),
    ('DISCHARGE_READ', 'DISCHARGE', 'READ', 'View discharges'),
    ('DISCHARGE_UPDATE', 'DISCHARGE', 'UPDATE', 'Update a discharge'),
    ('DISCHARGE_DELETE', 'DISCHARGE', 'DELETE', 'Delete a discharge'),

    ('FLOOR_CREATE', 'FLOOR', 'CREATE', 'Create a floor'),
    ('FLOOR_READ', 'FLOOR', 'READ', 'View floors'),
    ('FLOOR_UPDATE', 'FLOOR', 'UPDATE', 'Update a floor'),
    ('FLOOR_DELETE', 'FLOOR', 'DELETE', 'Delete a floor'),

    ('WARD_CREATE', 'WARD', 'CREATE', 'Create a ward'),
    ('WARD_READ', 'WARD', 'READ', 'View wards'),
    ('WARD_UPDATE', 'WARD', 'UPDATE', 'Update a ward'),
    ('WARD_DELETE', 'WARD', 'DELETE', 'Delete a ward'),

    ('ROOM_CREATE', 'ROOM', 'CREATE', 'Create a room'),
    ('ROOM_READ', 'ROOM', 'READ', 'View rooms'),
    ('ROOM_UPDATE', 'ROOM', 'UPDATE', 'Update a room'),
    ('ROOM_DELETE', 'ROOM', 'DELETE', 'Delete a room'),

    ('BED_CREATE', 'BED', 'CREATE', 'Create a bed'),
    ('BED_READ', 'BED', 'READ', 'View beds'),
    ('BED_UPDATE', 'BED', 'UPDATE', 'Update a bed'),
    ('BED_DELETE', 'BED', 'DELETE', 'Delete a bed'),

    ('BED_ASSIGNMENT_CREATE', 'BED_ASSIGNMENT', 'CREATE', 'Assign a bed'),
    ('BED_ASSIGNMENT_READ', 'BED_ASSIGNMENT', 'READ', 'View bed assignments'),
    ('BED_ASSIGNMENT_UPDATE', 'BED_ASSIGNMENT', 'UPDATE', 'Update a bed assignment'),
    ('BED_ASSIGNMENT_DELETE', 'BED_ASSIGNMENT', 'DELETE', 'Remove a bed assignment'),

    ('DIAGNOSIS_CREATE', 'DIAGNOSIS', 'CREATE', 'Create a diagnosis'),
    ('DIAGNOSIS_READ', 'DIAGNOSIS', 'READ', 'View diagnoses'),
    ('DIAGNOSIS_UPDATE', 'DIAGNOSIS', 'UPDATE', 'Update a diagnosis'),
    ('DIAGNOSIS_DELETE', 'DIAGNOSIS', 'DELETE', 'Delete a diagnosis'),

    ('MEDICAL_RECORD_CREATE', 'MEDICAL_RECORD', 'CREATE', 'Create a medical record'),
    ('MEDICAL_RECORD_READ', 'MEDICAL_RECORD', 'READ', 'View medical records'),
    ('MEDICAL_RECORD_UPDATE', 'MEDICAL_RECORD', 'UPDATE', 'Update a medical record'),
    ('MEDICAL_RECORD_DELETE', 'MEDICAL_RECORD', 'DELETE', 'Delete a medical record'),

    ('ALLERGY_CREATE', 'ALLERGY', 'CREATE', 'Create an allergy record'),
    ('ALLERGY_READ', 'ALLERGY', 'READ', 'View allergy records'),
    ('ALLERGY_UPDATE', 'ALLERGY', 'UPDATE', 'Update an allergy record'),
    ('ALLERGY_DELETE', 'ALLERGY', 'DELETE', 'Delete an allergy record'),

    ('MEDICATION_CREATE', 'MEDICATION', 'CREATE', 'Create a medication'),
    ('MEDICATION_READ', 'MEDICATION', 'READ', 'View medications'),
    ('MEDICATION_UPDATE', 'MEDICATION', 'UPDATE', 'Update a medication'),
    ('MEDICATION_DELETE', 'MEDICATION', 'DELETE', 'Delete a medication'),

    ('PRESCRIPTION_CREATE', 'PRESCRIPTION', 'CREATE', 'Create a prescription'),
    ('PRESCRIPTION_READ', 'PRESCRIPTION', 'READ', 'View prescriptions'),
    ('PRESCRIPTION_UPDATE', 'PRESCRIPTION', 'UPDATE', 'Update a prescription'),
    ('PRESCRIPTION_DELETE', 'PRESCRIPTION', 'DELETE', 'Delete a prescription'),

    ('PRESCRIPTION_ITEM_CREATE', 'PRESCRIPTION_ITEM', 'CREATE', 'Add a prescription item'),
    ('PRESCRIPTION_ITEM_READ', 'PRESCRIPTION_ITEM', 'READ', 'View prescription items'),
    ('PRESCRIPTION_ITEM_UPDATE', 'PRESCRIPTION_ITEM', 'UPDATE', 'Update a prescription item'),
    ('PRESCRIPTION_ITEM_DELETE', 'PRESCRIPTION_ITEM', 'DELETE', 'Remove a prescription item'),

    ('SERVICE_CATEGORY_CREATE', 'SERVICE_CATEGORY', 'CREATE', 'Create a service category'),
    ('SERVICE_CATEGORY_READ', 'SERVICE_CATEGORY', 'READ', 'View service categories'),
    ('SERVICE_CATEGORY_UPDATE', 'SERVICE_CATEGORY', 'UPDATE', 'Update a service category'),
    ('SERVICE_CATEGORY_DELETE', 'SERVICE_CATEGORY', 'DELETE', 'Delete a service category'),

    ('SERVICE_CREATE', 'SERVICE', 'CREATE', 'Create a service'),
    ('SERVICE_READ', 'SERVICE', 'READ', 'View services'),
    ('SERVICE_UPDATE', 'SERVICE', 'UPDATE', 'Update a service'),
    ('SERVICE_DELETE', 'SERVICE', 'DELETE', 'Delete a service'),

    ('BILL_CREATE', 'BILL', 'CREATE', 'Create a bill'),
    ('BILL_READ', 'BILL', 'READ', 'View bills'),
    ('BILL_UPDATE', 'BILL', 'UPDATE', 'Update a bill'),
    ('BILL_DELETE', 'BILL', 'DELETE', 'Delete a bill'),

    ('BILL_ITEM_CREATE', 'BILL_ITEM', 'CREATE', 'Add a bill item'),
    ('BILL_ITEM_READ', 'BILL_ITEM', 'READ', 'View bill items'),
    ('BILL_ITEM_UPDATE', 'BILL_ITEM', 'UPDATE', 'Update a bill item'),
    ('BILL_ITEM_DELETE', 'BILL_ITEM', 'DELETE', 'Remove a bill item'),

    ('PAYMENT_CREATE', 'PAYMENT', 'CREATE', 'Record a payment'),
    ('PAYMENT_READ', 'PAYMENT', 'READ', 'View payments'),
    ('PAYMENT_UPDATE', 'PAYMENT', 'UPDATE', 'Update payment details'),
    ('PAYMENT_DELETE', 'PAYMENT', 'DELETE', 'Delete a payment record'),

    ('USER_CREATE', 'USER', 'CREATE', 'Create a user'),
    ('USER_READ', 'USER', 'READ', 'View users'),
    ('USER_UPDATE', 'USER', 'UPDATE', 'Update a user'),
    ('USER_DELETE', 'USER', 'DELETE', 'Delete a user'),

    ('ROLE_CREATE', 'ROLE', 'CREATE', 'Create a role'),
    ('ROLE_READ', 'ROLE', 'READ', 'View roles'),
    ('ROLE_UPDATE', 'ROLE', 'UPDATE', 'Update a role'),
    ('ROLE_DELETE', 'ROLE', 'DELETE', 'Delete a role'),

    ('PERMISSION_CREATE', 'PERMISSION', 'CREATE', 'Create a permission'),
    ('PERMISSION_READ', 'PERMISSION', 'READ', 'View permissions'),
    ('PERMISSION_UPDATE', 'PERMISSION', 'UPDATE', 'Update a permission'),
    ('PERMISSION_DELETE', 'PERMISSION', 'DELETE', 'Delete a permission'),

    ('USER_ROLE_ASSIGN', 'USER_ROLE', 'ASSIGN', 'Assign a role to a user'),
    ('USER_ROLE_READ', 'USER_ROLE', 'READ', 'View user role assignments'),
    ('USER_ROLE_REVOKE', 'USER_ROLE', 'REVOKE', 'Revoke a role from a user'),

    ('ROLE_PERMISSION_ASSIGN', 'ROLE_PERMISSION', 'ASSIGN', 'Assign a permission to a role'),
    ('ROLE_PERMISSION_READ', 'ROLE_PERMISSION', 'READ', 'View role permissions'),
    ('ROLE_PERMISSION_REVOKE', 'ROLE_PERMISSION', 'REVOKE', 'Revoke a permission from a role'),

    ('DOCUMENT_CREATE', 'DOCUMENT', 'CREATE', 'Upload a document'),
    ('DOCUMENT_READ', 'DOCUMENT', 'READ', 'View documents'),
    ('DOCUMENT_UPDATE', 'DOCUMENT', 'UPDATE', 'Update document details'),
    ('DOCUMENT_DELETE', 'DOCUMENT', 'DELETE', 'Delete a document'),

    ('AUDIT_LOG_READ', 'AUDIT_LOG', 'READ', 'View audit logs'),

    ('REFRESH_TOKEN_REVOKE', 'REFRESH_TOKEN', 'REVOKE', 'Revoke a refresh token')
    ON CONFLICT (permission_name) DO NOTHING;
