DELETE FROM workouts WHERE user_id = '11111111-1111-1111-1111-111111111111';
INSERT INTO workouts (workout_id, user_id,workout_name,workout_description, created_at, updated_at)
VALUES ('21111111-1111-1111-1111-111111111111','11111111-1111-1111-1111-111111111111','testWorkout', 'a test workout', null, null);
