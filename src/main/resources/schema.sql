-- schema.sql
-- This file defines the database schema for the Candidate Module.
-- It uses JPA JOINED inheritance for users and candidates.

-- 1. Users Table (Base class for Inheritance)
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Candidates Table (Extends User via JPA JOINED strategy)
CREATE TABLE IF NOT EXISTS candidates (
    user_id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    location VARCHAR(255),
    profile_summary TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 3. Skills Table (Dictionary of skills)
CREATE TABLE IF NOT EXISTS skills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- 4. Candidate_Skills Table (Many-to-Many mapping with extra column)
CREATE TABLE IF NOT EXISTS candidate_skills (
    candidate_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    skill_level VARCHAR(50),
    PRIMARY KEY (candidate_id, skill_id),
    FOREIGN KEY (candidate_id) REFERENCES candidates(user_id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE
);

-- 5. Education Table (Composition: Candidate has Education)
CREATE TABLE IF NOT EXISTS education (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    candidate_id BIGINT NOT NULL,
    institution VARCHAR(255) NOT NULL,
    degree VARCHAR(255) NOT NULL,
    field_of_study VARCHAR(255),
    start_year INT NOT NULL,
    end_year INT,
    grade VARCHAR(50),
    FOREIGN KEY (candidate_id) REFERENCES candidates(user_id) ON DELETE CASCADE
);

-- 6. Experience Table (Composition: Candidate has Experience)
CREATE TABLE IF NOT EXISTS experience (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    candidate_id BIGINT NOT NULL,
    company VARCHAR(255) NOT NULL,
    job_title VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    is_current BOOLEAN DEFAULT FALSE,
    description TEXT,
    FOREIGN KEY (candidate_id) REFERENCES candidates(user_id) ON DELETE CASCADE
);

-- 7. Resumes Table (Composition: Candidate has Resume)
CREATE TABLE IF NOT EXISTS resumes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    candidate_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    file_path VARCHAR(500) NOT NULL, -- Configurable upload directory path
    upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (candidate_id) REFERENCES candidates(user_id) ON DELETE CASCADE
);

-- ==============================================================================
-- NOTE FOR MEMBER 2 (Company/Recruiter Module):
-- Member 1 does NOT own the full 'jobs' table. The structure below is a 
-- MINIMAL EXPECTED CONTRACT for viewing jobs. 
-- Member 2 is expected to create the actual jobs table and related entities.
-- ==============================================================================
-- CREATE TABLE IF NOT EXISTS jobs (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     title VARCHAR(255) NOT NULL,
--     company VARCHAR(255) NOT NULL,
--     location VARCHAR(255),
--     employment_type VARCHAR(100),
--     description TEXT,
--     requirements TEXT,
--     posted_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
-- );
