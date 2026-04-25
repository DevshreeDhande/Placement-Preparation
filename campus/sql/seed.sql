-- Insert initial companies
INSERT INTO companies (id, name, type, icon, ctc, openings, is_active) VALUES
(1, 'TCS', 'tech', '🔵', '3.5–6 LPA', 120, true),
(2, 'Infosys', 'tech', '🟢', '3.6–5 LPA', 200, true),
(3, 'Accenture', 'tech', '🔷', '4–7 LPA', 90, true),
(4, 'Amazon', 'product', '🛒', '25–45 LPA', 80, true),
(5, 'Microsoft', 'product', '🪟', '30–50 LPA', 60, true),
(6, 'Google', 'product', '🔍', '30–60 LPA', 40, true),
(7, 'HDFC Bank', 'finance', '🏦', '4–7 LPA', 50, true),
(8, 'Paytm', 'finance', '💸', '8–15 LPA', 25, true);

-- Insert roadmaps for those companies
INSERT INTO roadmaps (company_id, overview, process_steps, duration_weeks, key_topics, interview_tips) VALUES
(1, 'Tata Consultancy Services is India''s largest IT services company. They hire across roles like Developer, Tester, BA, and DevOps.', 'Online Test (NQT) → Technical Interview → Managerial Interview → HR Round', 'Week 1-2: Focus on Quantitative Aptitude|Week 3-4: Verbal Ability|Week 5-6: Programming Logic', 'Quantitative Aptitude,Verbal Ability,Programming Logic,Java/Python Basics', 'TCS NQT has a strict timer|Focus on accuracy over speed'),
(2, 'Infosys is a global digital services and consulting leader. Their InfyTQ platform offers specialized training paths.', 'Online Test (InfyTQ/HackWithInfy) → Technical Interview → HR Round', 'Week 1-2: Mathematical Ability|Week 3-4: Logical Reasoning', 'Mathematical Ability,Logical Reasoning,Java/Python OOP', 'InfyTQ certification gives bonus marks|Focus heavily on puzzles'),
(3, 'Accenture is a global professional services company. They hire for ASE and Analyst roles.', 'Cognitive + Technical Assessment → Coding Test → Communication Test → Interview', 'Week 1-2: Cognitive ability|Week 3-4: Technical MCQs', 'Abstract Reasoning,Networking Basics,DBMS & SQL', 'Accenture''s coding test allows multiple language choices|Communication test is new'),
(4, 'Amazon is one of the highest-paying recruiters on campus. They look for strong problem solvers.', 'Online Assessment (2 coding) → Phone Screen → Onsite (4 rounds)', 'Week 1-3: Data Structures deep dive|Week 4-6: Algorithm patterns', 'Arrays & Strings,Trees & Graphs,System Design,Leadership Principles', 'Every interview round includes behavioral questions|LeetCode medium is the sweet spot'),
(5, 'Microsoft hires for SDE, PM, and Research roles. They emphasize clean code, system design, and cultural fit.', 'Online Coding Test → Group Fly Round → Technical Interviews (3-4) → HR', 'Week 1-3: Core DSA|Week 4-5: Algorithm paradigms', 'DSA (Medium-Hard),System Design,Operating Systems', 'Microsoft values clean, production-quality code|Group fly round is a paper coding round'),
(6, 'Google is the most prestigious tech recruiter. They hire for SWE (L3) and STEP intern roles.', 'Online Coding Challenge → Phone Interview (2) → Onsite Virtual (4-5 rounds)', 'Week 1-4: Master DSA|Week 5-6: Algorithm design', 'Advanced DSA,Graph Algorithms,Dynamic Programming', 'Google interviews are harder than LeetCode hard|Interviewers care about thought process'),
(7, 'HDFC Bank recruits for Relationship Manager, Analyst, and Technology roles.', 'Aptitude Test → Group Discussion → Personal Interview → HR Round', 'Week 1-2: Financial awareness|Week 3-4: Quantitative aptitude', 'Banking Awareness,Data Interpretation,Communication Skills', 'Read The Economic Times daily|Know about HDFC Bank''s products'),
(8, 'Paytm (One97 Communications) hires for SDE, Product, and Business Development roles.', 'Online Coding Test → Technical Interview (2) → Hiring Manager Round', 'Week 1-3: DSA fundamentals|Week 4-5: System design', 'DSA (Medium Level),System Design,REST APIs', 'Paytm values practical coding|Understand how UPI, QR code works');

-- Update sequence for companies id
SELECT setval('companies_id_seq', (SELECT MAX(id) FROM companies));
