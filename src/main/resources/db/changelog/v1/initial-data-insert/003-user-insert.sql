
BEGIN;
INSERT INTO departments (department_code,
                         department_name,
                         description)
VALUES ('ADMIN',
        'Administration',
        'Hospital administration') ON CONFLICT (department_code) DO NOTHING;

WITH department AS (SELECT department_id FROM departments WHERE department_code = 'ADMIN')
INSERT
INTO staff (
    employee_no,
    first_name,
    last_name,
    department_id
)
SELECT '9632587569', 'Super', 'Admin', department_id
FROM department RETURNING staff_id;

INSERT INTO users (staff_id,
                   username,
                   password_hash,
                   active)
SELECT s.staff_id, 'super.admin@gmail.com', '$2a$10$FVwJJ3dsq54yA12Au.h3Gu3sbjHCam3xI0Br4cgP6xnQ593IEY/em', TRUE
FROM staff s
WHERE s.employee_no = '9632587569'
  AND NOT EXISTS (SELECT 1 FROM users u WHERE u.staff_id = s.staff_id) ON CONFLICT (username) DO NOTHING
RETURNING user_id, staff_id, username;

INSERT INTO user_roles (user_id, role_id)
SELECT u.user_id, r.role_id
FROM users u
         CROSS JOIN roles r
WHERE u.username = 'super.admin@gmail.com'
  AND UPPER(r.role_name) = 'SUPER_ADMIN' ON CONFLICT (user_id, role_id) DO NOTHING
RETURNING user_id, role_id, assigned_at;

COMMIT;
