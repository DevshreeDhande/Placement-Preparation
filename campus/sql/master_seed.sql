-- ═══════════════════════════════════════════════════════════════════════════════
-- GHRISTU PlacePrep – COMPLETE DATABASE SETUP (Run this ONCE in Railway)
-- Creates ALL tables in correct order + seeds all data
-- ═══════════════════════════════════════════════════════════════════════════════

-- ── 1. USERS (must be first — other tables reference this) ───────────────────
CREATE TABLE IF NOT EXISTS users (
    id              SERIAL PRIMARY KEY,
    email           VARCHAR(255) UNIQUE NOT NULL,
    name            VARCHAR(255) NOT NULL,
    password_hash   VARCHAR(255) NOT NULL,
    role            VARCHAR(20) NOT NULL DEFAULT 'student',
    prn             VARCHAR(50),
    branch          VARCHAR(100),
    batch           VARCHAR(10),
    cgpa            DECIMAL(4,2),
    phone           VARCHAR(20),
    avatar_url      TEXT,
    availability    VARCHAR(50) DEFAULT 'Open to opportunities',
    skills          TEXT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active       BOOLEAN DEFAULT TRUE
);

-- ── 2. COMPANIES ─────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS companies (
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    type            VARCHAR(50) NOT NULL,
    icon            VARCHAR(10),
    tags            VARCHAR(500),
    ctc             VARCHAR(50),
    openings        INTEGER DEFAULT 0,
    roadmap_overview    TEXT,
    roadmap_process     TEXT,
    roadmap_weeks       TEXT,
    roadmap_topics      TEXT,
    roadmap_tips        TEXT,
    is_active       BOOLEAN DEFAULT TRUE,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ── 3. CODING PROBLEMS ──────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS coding_problems (
    id              SERIAL PRIMARY KEY,
    title           VARCHAR(255) NOT NULL,
    difficulty      VARCHAR(20) NOT NULL,
    tags            VARCHAR(500),
    link            VARCHAR(500) DEFAULT '#',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ── 4. MOCK TESTS ───────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS mock_tests (
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    icon            VARCHAR(10),
    duration_min    INTEGER NOT NULL,
    total_questions INTEGER NOT NULL,
    difficulty      VARCHAR(20) NOT NULL,
    category        VARCHAR(50) NOT NULL,
    description     TEXT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ── 5. QUESTIONS ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS questions (
    id              SERIAL PRIMARY KEY,
    category        VARCHAR(100) NOT NULL,
    type            VARCHAR(20) NOT NULL,
    level           VARCHAR(20) NOT NULL,
    question_text   TEXT NOT NULL,
    option_a        TEXT NOT NULL,
    option_b        TEXT NOT NULL,
    option_c        TEXT DEFAULT '',
    option_d        TEXT DEFAULT '',
    correct_option  INTEGER NOT NULL DEFAULT 0,
    formula         TEXT DEFAULT '',
    explanation     TEXT DEFAULT '',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ── 6. PLACEMENT DRIVES (already exists, safe to skip) ──────────────────────
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

-- ── 7. FEEDBACK (depends on users) ──────────────────────────────────────────
CREATE TABLE IF NOT EXISTS feedback (
    id              SERIAL PRIMARY KEY,
    user_id         INTEGER REFERENCES users(id) ON DELETE SET NULL,
    category        VARCHAR(100),
    message         TEXT NOT NULL,
    response        TEXT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ── 8. USER CODING PROGRESS (depends on users + coding_problems) ────────────
CREATE TABLE IF NOT EXISTS user_coding_progress (
    id              SERIAL PRIMARY KEY,
    user_id         INTEGER REFERENCES users(id) ON DELETE CASCADE,
    problem_id      INTEGER REFERENCES coding_problems(id) ON DELETE CASCADE,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, problem_id)
);

-- ── 9. TEST ATTEMPTS (depends on users + mock_tests) ────────────────────────
CREATE TABLE IF NOT EXISTS test_attempts (
    id              SERIAL PRIMARY KEY,
    user_id         INTEGER REFERENCES users(id) ON DELETE CASCADE,
    test_id         INTEGER REFERENCES mock_tests(id) ON DELETE CASCADE,
    score           INTEGER,
    status          VARCHAR(20) DEFAULT 'new',
    started_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at    TIMESTAMP
);

-- ── 10. RESUMES ─────────────────────────────────────────────────────────────
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

-- ── 11. DAILY ACTIVITY ──────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS daily_activity (
    id              SERIAL PRIMARY KEY,
    user_id         INTEGER REFERENCES users(id) ON DELETE CASCADE,
    activity_date   DATE NOT NULL,
    questions_solved INTEGER DEFAULT 0,
    time_spent_min  INTEGER DEFAULT 0,
    UNIQUE(user_id, activity_date)
);


-- ═════════════════════════════════════════════════════════════════════════════
-- SEED DATA
-- ═════════════════════════════════════════════════════════════════════════════

-- ── SEED COMPANIES ──────────────────────────────────────────────────────────
INSERT INTO companies (name, type, icon, tags, ctc, openings)
SELECT * FROM (VALUES
  ('TCS',             'tech',    '🔵', 'Java,Python,Cloud',       '7 LPA',  120),
  ('Infosys',         'tech',    '🟢', 'Java,SAP,Testing',        '5.5 LPA', 80),
  ('Wipro',           'tech',    '⚙️', 'Java,.NET,DevOps',        '5 LPA',   60),
  ('Accenture',       'tech',    '🔷', 'Consulting,Cloud,Analytics','6.5 LPA',90),
  ('Goldman Sachs',   'finance', '💰', 'Java,C++,Algorithms',     '18 LPA',  15),
  ('Amazon',          'tech',    '🛒', 'DSA,System Design,AWS',   '28 LPA',  10),
  ('Capgemini',       'tech',    '🚀', 'Java,Cloud,Testing',      '4.5 LPA',100),
  ('Cognizant',       'tech',    '🔵', 'Java,.NET,Testing',       '4 LPA',   70)
) AS v(name, type, icon, tags, ctc, openings)
WHERE NOT EXISTS (SELECT 1 FROM companies LIMIT 1);

-- ── SEED PLACEMENT DRIVES ───────────────────────────────────────────────────
INSERT INTO placement_drives (company_name, drive_date, type, roles, ctc, status)
SELECT * FROM (VALUES
  ('TCS',           '2025-08-15'::DATE, 'tech',    'SDE, Analyst',      '7 LPA',   'confirmed'),
  ('Infosys',       '2025-08-22'::DATE, 'tech',    'Systems Engineer',  '5.5 LPA', 'confirmed'),
  ('Wipro',         '2025-09-01'::DATE, 'tech',    'Project Engineer',  '5 LPA',   'pending'),
  ('Accenture',     '2025-09-10'::DATE, 'tech',    'ASE, Analyst',      '6.5 LPA', 'confirmed'),
  ('Goldman Sachs', '2025-09-20'::DATE, 'finance', 'SDE, Risk Analyst', '18 LPA',  'pending')
) AS v(company_name, drive_date, type, roles, ctc, status)
WHERE NOT EXISTS (SELECT 1 FROM placement_drives LIMIT 1);

-- ── SEED CODING PROBLEMS ───────────────────────────────────────────────────
INSERT INTO coding_problems (id, title, difficulty, tags, link) VALUES
(1,  'Two Sum',                               'Easy',   'Arrays,HashMap',             '#'),
(2,  'Valid Parentheses',                     'Easy',   'Strings,Stack',              '#'),
(3,  'Reverse Linked List',                   'Easy',   'Linked List',                '#'),
(4,  'Maximum Subarray (Kadane''s)',           'Easy',   'Arrays,DP',                  '#'),
(5,  'Merge Two Sorted Lists',                'Easy',   'Linked List',                '#'),
(6,  'Best Time to Buy and Sell Stock',       'Easy',   'Arrays,Greedy',              '#'),
(7,  'Climbing Stairs',                       'Easy',   'DP,Math',                    '#'),
(8,  'Symmetric Tree',                        'Easy',   'Trees,BFS',                  '#'),
(9,  'Longest Common Subsequence',            'Medium', 'DP,Strings',                 '#'),
(10, 'Binary Tree Level Order Traversal',     'Medium', 'Trees,BFS',                  '#'),
(11, '3Sum',                                  'Medium', 'Arrays,Two Pointers',        '#'),
(12, 'Merge Intervals',                       'Medium', 'Arrays,Sorting',             '#'),
(13, 'Binary Tree Diameter',                  'Medium', 'Trees,DFS',                  '#'),
(14, 'Group Anagrams',                        'Medium', 'Strings,HashMap',            '#'),
(15, 'Coin Change',                           'Medium', 'DP',                         '#'),
(16, 'Rotate Image',                          'Medium', 'Arrays,Matrix',              '#'),
(17, 'Search in Rotated Sorted Array',        'Medium', 'Arrays,Binary Search',       '#'),
(18, 'Number of Islands',                     'Medium', 'Graphs,DFS',                 '#'),
(19, 'LRU Cache',                             'Medium', 'Design,HashMap',             '#'),
(20, 'Course Schedule',                       'Medium', 'Graphs,Topological Sort',    '#'),
(21, 'Longest Palindromic Substring',         'Medium', 'Strings,DP',                 '#'),
(22, 'Container With Most Water',             'Medium', 'Arrays,Two Pointers',        '#'),
(23, 'Word Break',                            'Medium', 'DP,Strings',                 '#'),
(24, 'Word Ladder',                           'Hard',   'Graphs,BFS',                 '#'),
(25, 'Median of Two Sorted Arrays',           'Hard',   'Arrays,Binary Search',       '#'),
(26, 'Trapping Rain Water',                   'Hard',   'Arrays,Stack,Two Pointers',  '#'),
(27, 'N-Queens',                              'Hard',   'Backtracking',               '#'),
(28, 'Serialize and Deserialize Binary Tree', 'Hard',   'Trees,Design',               '#'),
(29, 'Merge K Sorted Lists',                  'Hard',   'Linked List,Heap',           '#'),
(30, 'Graph Coloring',                        'Hard',   'Graphs,Backtracking',        '#'),
(31, 'Longest Valid Parentheses',             'Hard',   'Strings,DP,Stack',           '#'),
(32, 'Minimum Window Substring',              'Hard',   'Strings,Sliding Window',     '#')
ON CONFLICT (id) DO NOTHING;

SELECT setval('coding_problems_id_seq', (SELECT MAX(id) FROM coding_problems));

-- ── SEED MOCK TESTS ─────────────────────────────────────────────────────────
INSERT INTO mock_tests (id, name, duration_min, total_questions, difficulty, category, description, icon) VALUES
(1,  'TCS NQT Full Mock – 2025',  120, 100, 'medium', 'company', 'Complete NQT simulation with Aptitude, Reasoning, Verbal, and Coding sections.', '🔵'),
(2,  'Infosys Aptitude Test',       90,  80, 'medium', 'company', 'Quantitative, Logical Reasoning, and Verbal sections based on Infosys pattern.', '🟢'),
(3,  'Wipro WILP Assessment',       75,  70, 'easy',   'company', 'Assessment covering aptitude and basic programming for WILP program.', '⚙️'),
(4,  'Accenture Cognitive Test',     60,  50, 'medium', 'company', 'Cognitive ability, technical MCQs, and communication assessment.', '🔷'),
(5,  'Capgemini Game-Based Test',    45,  40, 'easy',   'company', 'Game-based aptitude and pseudo-code assessment.', '🚀'),
(6,  'Full Stack DSA Marathon',     180,  50, 'hard',   'full',    '50 DSA problems covering all topics — arrays to graphs.', '🔥'),
(7,  'Aptitude Speed Test',          30,  30, 'easy',   'topic',   'Quick-fire aptitude questions to test speed and accuracy.', '⏱️'),
(8,  'Amazon SDE OA Practice',       90,   4, 'hard',   'company', '2 coding problems + 2 behavioral questions matching Amazon OA pattern.', '🛒'),
(9,  'DBMS & SQL Fundamentals',      45,  40, 'medium', 'topic',   'Normalization, joins, transactions, and query optimization.', '🗄️'),
(10, 'Logical Reasoning Challenge',  60,  50, 'medium', 'topic',   'Puzzles, seating arrangement, blood relations, and coding-decoding.', '🧩')
ON CONFLICT (id) DO NOTHING;

SELECT setval('mock_tests_id_seq', (SELECT MAX(id) FROM mock_tests));

-- ── SEED FEEDBACK ───────────────────────────────────────────────────────────
INSERT INTO feedback (category, message)
SELECT * FROM (VALUES
  ('Coding Help',        'How do I approach dynamic programming problems? I keep getting stuck on the state transitions.'),
  ('General Feedback',   'Great platform! The TCS mock test was very close to the real pattern. Thanks to the team!'),
  ('Technical Question', 'Can someone explain the difference between clustered and non-clustered indexes in SQL?')
) AS v(category, message)
WHERE NOT EXISTS (SELECT 1 FROM feedback LIMIT 1);


-- ═══════════════════════════════════════════════════════════════════════════════
-- DONE! All 11 tables created. All seed data loaded.
-- Tables: users, companies, coding_problems, mock_tests, questions,
--         placement_drives, feedback, user_coding_progress, test_attempts,
--         resumes, daily_activity
-- ═══════════════════════════════════════════════════════════════════════════════
