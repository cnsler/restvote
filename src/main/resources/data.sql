INSERT INTO USERS (NAME, EMAIL, PASSWORD)
VALUES ('Admin', 'a@a', '{noop}adminPass'),
       ('User', 'u@u', '{noop}userPass'),
       ('New User', 'n@u', '{noop}newPass');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('ADMIN', 1),
       ('USER', 1),
       ('USER', 2),
       ('USER', 3);

INSERT INTO RESTAURANT (TITLE)
VALUES ('Огонек'),
       ('Рыбак'),
       ('Кавказ');

INSERT INTO VOTE (VOTE_DATE, RESTAURANT_ID, USER_ID)
VALUES (now() - 2, 1, 1),
       (now() - 2, 1, 2),
       (now() - 2, 2, 3),
       (now() - 1, 3, 1),
       (now() - 1, 3, 2),
       (now() - 1, 3, 3),
       (now(), 1, 3)