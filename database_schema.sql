-- VerifyDocs Database Schema
-- This file shows the database structure that will be created by JPA

-- 1. PROVINCES TABLE (Location Storage)
CREATE TABLE provinces (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL
);

-- 2. INSTITUTIONS TABLE
CREATE TABLE institutions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    status VARCHAR(255) NOT NULL,
    province_id BIGINT NOT NULL,
    FOREIGN KEY (province_id) REFERENCES provinces(id)
);

-- 3. INSTITUTION_PROFILES TABLE (One-to-One with Institution)
CREATE TABLE institution_profiles (
    id BIGSERIAL PRIMARY KEY,
    address VARCHAR(255),
    phone VARCHAR(255),
    website VARCHAR(255),
    description TEXT,
    institution_id BIGINT UNIQUE NOT NULL,
    FOREIGN KEY (institution_id) REFERENCES institutions(id)
);

-- 4. USERS TABLE
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    institution_id BIGINT NOT NULL,
    FOREIGN KEY (institution_id) REFERENCES institutions(id)
);

-- 5. DOCUMENTS TABLE
CREATE TABLE documents (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    hash_value VARCHAR(255) UNIQUE NOT NULL,
    verification_code VARCHAR(255) UNIQUE NOT NULL,
    issue_date TIMESTAMP NOT NULL,
    institution_id BIGINT NOT NULL,
    FOREIGN KEY (institution_id) REFERENCES institutions(id)
);

-- 6. VERIFICATION_LOGS TABLE
CREATE TABLE verification_logs (
    id BIGSERIAL PRIMARY KEY,
    verification_time TIMESTAMP NOT NULL,
    ip_address VARCHAR(255),
    result VARCHAR(255) NOT NULL,
    document_id BIGINT NOT NULL,
    FOREIGN KEY (document_id) REFERENCES documents(id)
);

-- 7. USER_DOCUMENT_ACCESS TABLE (Many-to-Many Join Table)
CREATE TABLE user_document_access (
    user_id BIGINT NOT NULL,
    document_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, document_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (document_id) REFERENCES documents(id)
);

-- Sample Data for Testing

-- Insert Provinces
INSERT INTO provinces (code, name) VALUES ('KGL', 'Kigali');
INSERT INTO provinces (code, name) VALUES ('EST', 'Eastern');
INSERT INTO provinces (code, name) VALUES ('WST', 'Western');

-- Insert Institutions
INSERT INTO institutions (name, email, status, province_id) 
VALUES ('AUCA', 'info@auca.ac.rw', 'ACTIVE', 1);

INSERT INTO institutions (name, email, status, province_id) 
VALUES ('UR', 'info@ur.ac.rw', 'ACTIVE', 1);

-- Insert Users
INSERT INTO users (email, password, role, full_name, institution_id) 
VALUES ('admin@auca.ac.rw', 'password123', 'ADMIN', 'John Doe', 1);

INSERT INTO users (email, password, role, full_name, institution_id) 
VALUES ('user@auca.ac.rw', 'password123', 'USER', 'Jane Smith', 1);

-- Query Examples

-- 1. Get all users from Kigali province using province code
SELECT u.* FROM users u
JOIN institutions i ON u.institution_id = i.id
JOIN provinces p ON i.province_id = p.id
WHERE p.code = 'KGL';

-- 2. Get all users from Kigali province using province name
SELECT u.* FROM users u
JOIN institutions i ON u.institution_id = i.id
JOIN provinces p ON i.province_id = p.id
WHERE p.name = 'Kigali';

-- 3. Get institution with its profile (One-to-One)
SELECT i.*, ip.* FROM institutions i
LEFT JOIN institution_profiles ip ON i.id = ip.institution_id
WHERE i.id = 1;

-- 4. Get all documents for an institution (One-to-Many)
SELECT d.* FROM documents d
WHERE d.institution_id = 1;

-- 5. Get all users who can access a specific document (Many-to-Many)
SELECT u.* FROM users u
JOIN user_document_access uda ON u.id = uda.user_id
WHERE uda.document_id = 1;
