INSERT INTO USERS (NAME, EMAIL, PASSWORD)
VALUES ('Admin', 'a@a', '{noop}adminPass'),
       ('User', 'u@u', '{noop}userPass');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('ADMIN', 1),
       ('USER', 1),
       ('USER', 2);

INSERT INTO RESTAURANT (TITLE)
VALUES ('Огонек'),
       ('Рыбак'),
       ('Кавказ')