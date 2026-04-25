-- Insert mock tests
INSERT INTO mock_tests (id, name, duration_min, total_questions, difficulty, category, description, icon) VALUES
(1, 'TCS NQT Full Mock – 2025', 120, 100, 'medium', 'company', 'Complete NQT simulation with Aptitude, Reasoning, Verbal, and Coding sections.', '🔵'),
(2, 'Infosys Aptitude Test', 90, 80, 'medium', 'company', 'Quantitative, Logical Reasoning, and Verbal sections based on Infosys pattern.', '🟢'),
(3, 'Wipro WILP Assessment', 75, 70, 'easy', 'company', 'Assessment covering aptitude and basic programming for WILP program.', '⚙️'),
(4, 'Accenture Cognitive Test', 60, 50, 'medium', 'company', 'Cognitive ability, technical MCQs, and communication assessment.', '🔷'),
(5, 'Capgemini Game-Based Test', 45, 40, 'easy', 'company', 'Game-based aptitude and pseudo-code assessment.', '🚀'),
(6, 'Full Stack DSA Marathon', 180, 50, 'hard', 'full', '50 DSA problems covering all topics — arrays to graphs.', '🔥'),
(7, 'Aptitude Speed Test', 30, 30, 'easy', 'topic', 'Quick-fire aptitude questions to test speed and accuracy.', '⏱️'),
(8, 'Amazon SDE OA Practice', 90, 4, 'hard', 'company', '2 coding problems + 2 behavioral questions matching Amazon OA pattern.', '🛒'),
(9, 'DBMS & SQL Fundamentals', 45, 40, 'medium', 'topic', 'Normalization, joins, transactions, and query optimization.', '🗄️'),
(10, 'Logical Reasoning Challenge', 60, 50, 'medium', 'topic', 'Puzzles, seating arrangement, blood relations, and coding-decoding.', '🧩');

-- Update sequence for mock_tests id
SELECT setval('mock_tests_id_seq', (SELECT MAX(id) FROM mock_tests));
