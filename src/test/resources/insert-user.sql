DELETE FROM users WHERE user_id='11111111-1111-1111-1111-111111111111';
INSERT INTO users (user_id, user_first_name, user_last_name, user_login_id, password, created_at, unit_preference)
VALUES ('11111111-1111-1111-1111-111111111111','Test','User', 'test@example.com', '$2a$10$NHCAD9Bjvxxdyet9FQp8hOayPiGJJ19tPHsjtucZ8tX96DOssegEu', null, 'KG');
