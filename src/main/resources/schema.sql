DROP TABLE IF EXISTS USERS;
DROP TABLE IF EXISTS TIME_STAT;

CREATE TABLE USERS
(
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    LOGIN VARCHAR(255) NOT NULL UNIQUE,
    FIRST_NAME VARCHAR(255),
    MIDDLE_NAME VARCHAR(255),
    LAST_NAME VARCHAR(255),
    AGE INT
);

CREATE TABLE TIME_STAT
(
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    METHOD VARCHAR(255) NOT NULL,
    started_at_mills BIGINT NOT NULL,
    ended_at_mills BIGINT NOT NULL,
    duration_mills BIGINT NOT NULL,
    is_async BOOL NOT NULL
);
