CREATE TABLE student (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(255) DEFAULT NULL,
    code TEXT DEFAULT NULL,
    address VARCHAR(255) DEFAULT NULL,
    phone VARCHAR(20) DEFAULT NULL,
    age INT DEFAULT NULL,
    score INT DEFAULT NULL
);

COMMENT ON COLUMN student.id IS 'Auto-increment ID';
COMMENT ON COLUMN student.name IS 'Student full name';
COMMENT ON COLUMN student.code IS 'Student code';
COMMENT ON COLUMN student.address IS 'Address';
COMMENT ON COLUMN student.phone IS 'Phone number';
COMMENT ON COLUMN student.age IS 'Age';
COMMENT ON COLUMN student.score IS 'Score';
