-- Unitime Database Schema for University Calendar App

-- Enums
CREATE TYPE appointment_type AS ENUM ('COURSE', 'EXAM', 'TD', 'TP', 'TEST','SPECIAL_EVENT');
CREATE TYPE promotion_level AS ENUM ('L1', 'L2', 'L3', 'M1', 'M2', 'D1', 'D2', 'D3');
CREATE TYPE user_role AS ENUM ('STUDENT', 'GROUP_ADMIN', 'SECTION_ADMIN', 'SUPER_ADMIN');


-- Promotions table (academic levels like L1, L2, etc.)
CREATE TABLE IF NOT EXISTS promotions (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL, -- e.g., "Computer Science L1", "Mathematics L2"
    level promotion_level NOT NULL, -- L1, L2, L3, etc.
    field VARCHAR(100) NOT NULL, -- "Computer Science", "Mathematics", "Physics"
    access_code VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    UNIQUE(level, field) -- Prevent duplicate "L1 Computer Science"
);

-- Groups table (specific class groups within a promotion)
CREATE TABLE IF NOT EXISTS groups (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL, -- e.g., "Group A", "Group 1"
    promotion_id INTEGER REFERENCES promotions(id) ON DELETE CASCADE,
    access_code VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    UNIQUE(name, promotion_id) -- Prevent duplicate group names within same promotion
);


-- Users table
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    role user_role DEFAULT 'student',
    group_id INTEGER REFERENCES groups(id),
    promotion_id INTEGER REFERENCES promotions(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- User group assignments (one user, one group at a time)
CREATE TABLE IF NOT EXISTS user_groups(
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    group_id INTEGER REFERENCES groups(id) ON DELETE CASCADE,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    UNIQUE(user_id) -- Ensures one user can only be in one group
);

-- Appointments/Events table (unified table for all calendar events)
CREATE TABLE IF NOT EXISTS appointments(
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    location VARCHAR(255), -- Simple string as requested
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    event_type appointment_type NOT NULL,
    recurrence_rule TEXT, -- String for sfCalendar widget (RRULE format)
    created_by INTEGER REFERENCES users(id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Junction table for appointment visibility (which groups/promotions can see which appointments)
CREATE TABLE IF NOT EXISTS appointment_visibility (
    id SERIAL PRIMARY KEY,
    appointment_id INTEGER REFERENCES appointments(id) ON DELETE CASCADE,
    group_id INTEGER REFERENCES groups(id) ON DELETE CASCADE,
    promotion_id INTEGER REFERENCES promotions(id) ON DELETE CASCADE,

    -- Constraint to ensure either group_id or promotion_id is set, but not both
    CONSTRAINT check_visibility_target CHECK (
        (group_id IS NOT NULL AND promotion_id IS NULL) OR
        (group_id IS NULL AND promotion_id IS NOT NULL)
    )
);

-- Indexes for better performance
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);
CREATE INDEX IF NOT EXISTS idx_promotions_level ON promotions(level);
CREATE INDEX IF NOT EXISTS idx_promotions_field ON promotions(field);
CREATE INDEX IF NOT EXISTS idx_appointments_start_time ON appointments(start_time);
CREATE INDEX IF NOT EXISTS idx_appointments_end_time ON appointments(end_time);
CREATE INDEX IF NOT EXISTS idx_appointments_event_type ON appointments(event_type);
CREATE INDEX IF NOT EXISTS idx_appointments_created_by ON appointments(created_by);
CREATE INDEX IF NOT EXISTS idx_appointment_visibility_appointment ON appointment_visibility(appointment_id);
CREATE INDEX IF NOT EXISTS idx_appointment_visibility_group ON appointment_visibility(group_id);
CREATE INDEX IF NOT EXISTS idx_appointment_visibility_promotion ON appointment_visibility(promotion_id);
CREATE INDEX IF NOT EXISTS idx_user_groups_user ON user_groups(user_id);
CREATE INDEX IF NOT EXISTS idx_user_groups_group ON user_groups(group_id);

-- Trigger to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_appointments_updated_at BEFORE UPDATE ON appointments
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Sample data insertion queries
INSERT INTO promotions (name, level, field, access_code, description) VALUES ('Computer Science L1', 'L1', 'Computer Science', 'L1-CS-2024', 'First Year Computer Science'),
('Mathematics L1', 'L1', 'Mathematics', 'L1-MATH-2024', 'First Year Mathematics'),
 ('Computer Science L2', 'L2', 'Computer Science', 'L2-CS-2024', 'Second Year Computer Science'),
 ('Physics L3', 'L3', 'Physics', 'L3-PHYS-2024', 'Third Year Physics');

 INSERT INTO groups (name, promotion_id, access_code, description) VALUES
('Group A', 1, 'L1-CS-A-2024', 'L1 Computer Science Group A'),
('Group B', 1, 'L1-CS-B-2024', 'L1 Computer Science Group B'),
 ('Group A', 2, 'L1-MATH-A-2024', 'L1 Mathematics Group A');

 INSERT INTO users (email, password, first_name, last_name, role) VALUES
 ('student@university.dz', 'hashed_password', 'Ahmed', 'Benali', 'STUDENT'),
('admin@university.dz', 'hashed_password', 'Fatima', 'Mokrane', 'GROUP_ADMIN'),
('super@university.dz', 'hashed_password', 'Karim', 'Zidane', 'SUPER_ADMIN');