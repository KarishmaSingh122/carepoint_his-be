BEGIN;

INSERT INTO departments (
    department_code,
    department_name,
    description
)
VALUES (
           'SYSTEM',
           'System Administration',
           'Reserved department for platform-level administration. Independent of hospital operations.'
       )
    ON CONFLICT (department_code) DO NOTHING;

INSERT INTO roles (
    role_name,
    description
)
VALUES (
           'SUPER_ADMIN',
           'Permanent platform-level administrator with global HIMS access.'
       )
    ON CONFLICT DO NOTHING;

INSERT INTO staff (
    employee_no,
    first_name,
    last_name,
    department_id
)
SELECT
    'SYS-000001',
    'System',
    'Administrator',
    d.department_id
FROM departments d
WHERE d.department_code = 'SYSTEM'
    ON CONFLICT DO NOTHING;

INSERT INTO users (
    staff_id,
    username,
    password_hash,
    active
)
SELECT
    s.staff_id,
    'super.admin@gmail.com',
    '$2a$10$FVwJJ3dsq54yA12Au.h3Gu3sbjHCam3xI0Br4cgP6xnQ593IEY/em',
    TRUE
FROM staff s
WHERE s.employee_no = 'SYS-000001'
    ON CONFLICT DO NOTHING;

INSERT INTO user_roles (
    user_id,
    role_id
)
SELECT
    u.user_id,
    r.role_id
FROM users u
         CROSS JOIN roles r
WHERE u.username = 'super.admin@gmail.com'
  AND UPPER(r.role_name) = 'SUPER_ADMIN'
    ON CONFLICT (user_id, role_id) DO NOTHING;

CREATE OR REPLACE FUNCTION protect_super_admin_role()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE'
       AND UPPER(OLD.role_name) = 'SUPER_ADMIN' THEN
        RAISE EXCEPTION 'SUPER_ADMIN role cannot be deleted';
END IF;

    IF TG_OP = 'UPDATE'
       AND (
           UPPER(OLD.role_name) = 'SUPER_ADMIN'
           OR UPPER(NEW.role_name) = 'SUPER_ADMIN'
       ) THEN
        RAISE EXCEPTION 'SUPER_ADMIN role cannot be modified';
END IF;

RETURN OLD;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_protect_super_admin_role ON roles;

CREATE TRIGGER trg_protect_super_admin_role
    BEFORE UPDATE OR DELETE ON roles
FOR EACH ROW
EXECUTE FUNCTION protect_super_admin_role();

CREATE OR REPLACE FUNCTION protect_initial_super_admin()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE'
       AND OLD.username = 'super.admin@gmail.com' THEN
        RAISE EXCEPTION 'Initial Super Admin account cannot be deleted';
END IF;

    IF TG_OP = 'UPDATE'
       AND OLD.username = 'super.admin@gmail.com'
       AND (
           NEW.username <> OLD.username
           OR NEW.staff_id <> OLD.staff_id
           OR NEW.active IS DISTINCT FROM TRUE
       ) THEN
        RAISE EXCEPTION 'Initial Super Admin account is protected';
END IF;

    IF TG_OP = 'DELETE' THEN
        RETURN OLD;
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_protect_initial_super_admin ON users;

CREATE TRIGGER trg_protect_initial_super_admin
    BEFORE UPDATE OR DELETE ON users
FOR EACH ROW
EXECUTE FUNCTION protect_initial_super_admin();

CREATE OR REPLACE FUNCTION protect_super_admin_assignment()
RETURNS TRIGGER AS $$
DECLARE
protected_user BOOLEAN;
    protected_role BOOLEAN;
BEGIN
SELECT EXISTS (
    SELECT 1
    FROM users
    WHERE user_id = OLD.user_id
      AND username = 'super.admin@gmail.com'
) INTO protected_user;

SELECT EXISTS (
    SELECT 1
    FROM roles
    WHERE role_id = OLD.role_id
      AND UPPER(role_name) = 'SUPER_ADMIN'
) INTO protected_role;

IF protected_user AND protected_role THEN
        RAISE EXCEPTION 'Initial Super Admin role assignment cannot be modified or removed';
END IF;

RETURN OLD;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_protect_super_admin_assignment ON user_roles;

CREATE TRIGGER trg_protect_super_admin_assignment
    BEFORE UPDATE OR DELETE ON user_roles
FOR EACH ROW
EXECUTE FUNCTION protect_super_admin_assignment();

COMMIT;

SELECT
    u.user_id,
    u.username,
    u.active,
    s.employee_no,
    s.first_name,
    s.last_name,
    d.department_code,
    d.department_name,
    r.role_name
FROM users u
         JOIN staff s ON s.staff_id = u.staff_id
         JOIN departments d ON d.department_id = s.department_id
         JOIN user_roles ur ON ur.user_id = u.user_id
         JOIN roles r ON r.role_id = ur.role_id
WHERE u.username = 'super.admin@gmail.com'
  AND r.role_name = 'SUPER_ADMIN';