-- ═══════════════════════════════════════════════════════════════════════════════
-- GHRISTU PlacePrep – Database Schema (PostgreSQL / Supabase)
-- ═══════════════════════════════════════════════════════════════════════════════

-- ── USERS & AUTH ──────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS users (
    id              SERIAL PRIMARY KEY,
    email           VARCHAR(255) UNIQUE NOT NULL,
    name            VARCHAR(255) NOT NULL,
    password_hash   VARCHAR(255) NOT NULL,
    role            VARCHAR(20) NOT NULL DEFAULT 'student' CHECK (role IN ('student','coordinator','recruiter')),
    prn             VARCHAR(50),
    branch          VARCHAR(100),
    batch           VARCHAR(10),
    cgpa            DECIMAL(4,2),
    phone           VARCHAR(20),
    avatar_url      TEXT,
    availability    VARCHAR(50) DEFAULT 'Open to opportunities',
    skills          TEXT[],
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active       BOOLEAN DEFAULT TRUE
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);

-- ── COMPANIES ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS companies (
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    type            VARCHAR(50) NOT NULL,
    icon            VARCHAR(10),
    tags            TEXT[],
    ctc             VARCHAR(50),
    openings        INTEGER DEFAULT 0,
    roadmap_overview    TEXT,
    roadmap_process     TEXT,
    roadmap_weeks       TEXT[],
    roadmap_topics      TEXT[],
    roadmap_tips        TEXT[],
    is_active       BOOLEAN DEFAULT TRUE,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ── QUESTIONS (Aptitude + Technical) ──────────────────────────────────────────
CREATE TABLE IF NOT EXISTS questions (
    id              SERIAL PRIMARY KEY,
    category        VARCHAR(100) NOT NULL,     -- 'Quantitative Aptitude', 'DBMS & SQL', etc.
    type            VARCHAR(20) NOT NULL CHECK (type IN ('aptitude','technical')),
    level           VARCHAR(20) NOT NULL CHECK (level IN ('beginner','moderate','hard')),
    question_text   TEXT NOT NULL,
    option_a        TEXT NOT NULL,
    option_b        TEXT NOT NULL,
    option_c        TEXT NOT NULL,
    option_d        TEXT NOT NULL,
    correct_option  INTEGER NOT NULL CHECK (correct_option BETWEEN 0 AND 3),
    formula         TEXT,
    explanation     TEXT,
    steps           TEXT[],
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_questions_category ON questions(category);
CREATE INDEX idx_questions_type ON questions(type);
CREATE INDEX idx_questions_level ON questions(level);

-- ── CODING PROBLEMS ──────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS coding_problems (
    id              SERIAL PRIMARY KEY,
    title           VARCHAR(255) NOT NULL,
    difficulty      VARCHAR(20) NOT NULL CHECK (difficulty IN ('Easy','Medium','Hard')),
    description     TEXT,
    tags            TEXT[],
    hints           TEXT[],
    solution_url    TEXT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ── MOCK TESTS ───────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS mock_tests (
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    icon            VARCHAR(10),
    duration_min    INTEGER NOT NULL,
    question_count  INTEGER NOT NULL,
    difficulty      VARCHAR(20) NOT NULL,
    category        VARCHAR(20) NOT NULL CHECK (category IN ('company','topic','full')),
    description     TEXT,
    is_active       BOOLEAN DEFAULT TRUE,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ── MOCK TEST QUESTIONS (link table) ─────────────────────────────────────────
CREATE TABLE IF NOT EXISTS mock_test_questions (
    id              SERIAL PRIMARY KEY,
    test_id         INTEGER REFERENCES mock_tests(id) ON DELETE CASCADE,
    question_id     INTEGER REFERENCES questions(id) ON DELETE CASCADE,
    sort_order      INTEGER DEFAULT 0
);

-- ── USER PROGRESS ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS user_progress (
    id              SERIAL PRIMARY KEY,
    user_id         INTEGER REFERENCES users(id) ON DELETE CASCADE,
    category        VARCHAR(100) NOT NULL,
    type            VARCHAR(20) NOT NULL,
    level           VARCHAR(20),
    questions_attempted INTEGER DEFAULT 0,
    correct_count   INTEGER DEFAULT 0,
    total_time_sec  INTEGER DEFAULT 0,
    last_attempted  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, category, type, level)
);

CREATE INDEX idx_progress_user ON user_progress(user_id);

-- ── CODING PROGRESS ──────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS user_coding_progress (
    id              SERIAL PRIMARY KEY,
    user_id         INTEGER REFERENCES users(id) ON DELETE CASCADE,
    problem_id      INTEGER REFERENCES coding_problems(id) ON DELETE CASCADE,
    status          VARCHAR(20) DEFAULT 'unsolved' CHECK (status IN ('unsolved','attempted','solved')),
    attempts        INTEGER DEFAULT 0,
    solved_at       TIMESTAMP,
    UNIQUE(user_id, problem_id)
);

-- ── TEST ATTEMPTS ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS test_attempts (
    id              SERIAL PRIMARY KEY,
    user_id         INTEGER REFERENCES users(id) ON DELETE CASCADE,
    test_id         INTEGER REFERENCES mock_tests(id) ON DELETE CASCADE,
    score           DECIMAL(5,2),
    total_questions INTEGER,
    correct_count   INTEGER,
    time_taken_sec  INTEGER,
    answers         JSONB,
    started_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at    TIMESTAMP
);

CREATE INDEX idx_attempts_user ON test_attempts(user_id);

-- ── FEEDBACK & DOUBTS ────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS feedback (
    id              SERIAL PRIMARY KEY,
    user_id         INTEGER REFERENCES users(id) ON DELETE SET NULL,
    category        VARCHAR(100),
    message         TEXT NOT NULL,
    response        TEXT,
    responded_by    INTEGER REFERENCES users(id),
    status          VARCHAR(20) DEFAULT 'open' CHECK (status IN ('open','answered','closed')),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ── RESUME DATA ──────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS resumes (
    id              SERIAL PRIMARY KEY,
    user_id         INTEGER REFERENCES users(id) ON DELETE CASCADE,
    full_name       VARCHAR(255),
    email           VARCHAR(255),
    phone           VARCHAR(20),
    objective       TEXT,
    education       TEXT,
    skills          TEXT,
    projects        TEXT,
    template        VARCHAR(50) DEFAULT 'classic',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id)
);

-- ── CERTIFICATIONS ───────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS certifications (
    id              SERIAL PRIMARY KEY,
    user_id         INTEGER REFERENCES users(id) ON DELETE CASCADE,
    name            VARCHAR(255) NOT NULL,
    issued_date     VARCHAR(100),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ── DAILY STREAKS ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS daily_activity (
    id              SERIAL PRIMARY KEY,
    user_id         INTEGER REFERENCES users(id) ON DELETE CASCADE,
    activity_date   DATE NOT NULL,
    questions_solved INTEGER DEFAULT 0,
    time_spent_min  INTEGER DEFAULT 0,
    UNIQUE(user_id, activity_date)
);

CREATE INDEX idx_daily_user ON daily_activity(user_id);

-- ── PLACEMENT DRIVES ─────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS placement_drives (
    id              SERIAL PRIMARY KEY,
    company_name    VARCHAR(255) NOT NULL,
    drive_date      DATE NOT NULL,
    type            VARCHAR(100),
    roles           VARCHAR(255),
    ctc             VARCHAR(100),
    status          VARCHAR(50) DEFAULT 'pending',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
