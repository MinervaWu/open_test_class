DELETE FROM user;

INSERT INTO user (id, user_name, account, password, role, user_status)
VALUES (1, 'Admin', 'Admin', 'QWRtaW4=', 0, 1),
       (2, 'teacher', 'teacher', 'dGVhY2hlcg==', 2, 1),
       (3, 'student', 'student', 'c3R1ZGVudA==', 1, 1);


DELETE FROM lesson;
INSERT INTO lesson (id, lesson_name, teacher_name, info, lesson_status, create_user_id, update_user_id)
VALUES (1, 'lesson1', 'teacher1', 'Monday', 1, 1, 1),
       (2, 'lesson2', 'teacher2', 'Tuesday', 1, 1, 1),
       (3, 'lesson3', 'teacher3', 'Wednesday', 1, 1, 1);
