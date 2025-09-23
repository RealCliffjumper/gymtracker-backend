DELETE FROM exercises WHERE exercise_name LIKE '%intTest%';
INSERT INTO exercises (exercise_id, user_id,exercise_name,exercise_description, muscle_group, equipment)
VALUES ('22222222-2222-2222-2222-222222222222','11111111-1111-1111-1111-111111111111','intTest1', 'a test exercise', 'LOWER_BACK', 'BARBELL'),
       ('22222222-2322-2222-2222-222222222222','11111111-1111-1111-1111-111111111111','intTest2', 'a test exercise', 'BACK', 'KETTLEBELL'),
       ('22222222-2422-2222-2222-222222222222',null,'intTest3', 'a test exercise', 'LEGS', 'BODYWEIGHT'),
       ('22222222-2522-2222-2222-222222222222',null,'intTest4', 'a test exercise', 'CHEST', 'DUMBBELL'),
       ('22222222-2622-2222-2222-222222222222','11111111-1111-1111-1111-111111111111','intTest5', 'a test exercise', 'UPPER_BACK', 'BARBELL');
