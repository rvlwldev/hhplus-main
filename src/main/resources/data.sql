INSERT INTO user (user_id, point) VALUES (1, 1000);
INSERT INTO user (user_id, point) VALUES (2, 2000);

INSERT INTO concert (concert_id, price, name) VALUES (1, 30000, "허재의 카드마술쇼");
INSERT INTO concert (concert_id, price, name) VALUES (2, 50000, "하헌우의 라이브 코딩쇼");

INSERT INTO concert_schedule
(schedule_id, concert_id, start_date_time, end_date_time, maximum_audience_count)
VALUES
(1, 1, '2024-12-01 18:00:00', '2024-12-01 20:00:00', 50),
(2, 1, '2024-12-02 18:00:00', '2024-12-02 20:00:00', 50),
(3, 2, '2024-12-10 19:00:00', '2024-12-10 21:30:00', 50),
(4, 2, '2024-12-11 19:00:00', '2024-12-11 21:30:00', 50);

--INSERT INTO seat (seat_id, concert_schedule_id, user_id) VALUES
--(1,  1, NULL), (2,  1, NULL), (3,  1, NULL), (4,  1, NULL), (5,  1, NULL),
--(6,  1, NULL), (7,  1, NULL), (8,  1, NULL), (9,  1, NULL), (10, 1, NULL),
--(11, 1, NULL), (12, 1, NULL), (13, 1, NULL), (14, 1, NULL), (15, 1, NULL),
--(16, 1, NULL), (17, 1, NULL), (18, 1, NULL), (19, 1, NULL), (20, 1, NULL),
--(21, 1, NULL), (22, 1, NULL), (23, 1, NULL), (24, 1, NULL), (25, 1, NULL),
--(26, 1, NULL), (27, 1, NULL), (28, 1, NULL), (29, 1, NULL), (30, 1, NULL),
--(31, 1, NULL), (32, 1, NULL), (33, 1, NULL), (34, 1, NULL), (35, 1, NULL),
--(36, 1, NULL), (37, 1, NULL), (38, 1, NULL), (39, 1, NULL), (40, 1, NULL),
--(41, 1, NULL), (42, 1, NULL), (43, 1, NULL), (44, 1, NULL), (45, 1, NULL),
--(46, 1, NULL), (47, 1, NULL), (48, 1, NULL), (49, 1, NULL), (50, 1, NULL);