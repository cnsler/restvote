INSERT INTO USERS (NAME, EMAIL, PASSWORD)
VALUES ('Admin', 'a@a', '{noop}adminPass'),
       ('User', 'u@u', '{noop}userPass'),
       ('New User', 'n@u', '{noop}newPass');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('ADMIN', 1),
       ('USER', 1),
       ('USER', 2),
       ('USER', 3);

INSERT INTO RESTAURANT (NAME)
VALUES ('Огонек'),
       ('Рыбак'),
       ('Кавказ');

INSERT INTO VOTE (VOTE_DATE, RESTAURANT_ID, USER_ID)
VALUES (CURRENT_DATE - 2, 1, 1),
       (CURRENT_DATE - 2, 1, 2),
       (CURRENT_DATE - 2, 2, 3),
       (CURRENT_DATE - 1, 3, 1),
       (CURRENT_DATE - 1, 3, 2),
       (CURRENT_DATE - 1, 3, 3),
       (CURRENT_DATE, 1, 3);

INSERT INTO MEAL (NAME, MEAL_DATE, PRICE, RESTAURANT_ID)
VALUES ('Стейк', CURRENT_DATE - 1, 790, 1),
       ('Овощи', CURRENT_DATE - 1, 200, 1),
       ('Шашлык', CURRENT_DATE, 550, 1),
       ('Овощи', CURRENT_DATE, 250, 1),
       ('Уха', CURRENT_DATE, 310, 2),
       ('Язь', CURRENT_DATE, 560, 2),
       ('Харчо', CURRENT_DATE - 1, 290, 3),
       ('Хачапури', CURRENT_DATE - 1, 450, 3),
       ('Хинкали', CURRENT_DATE, 490, 3)