CREATE TABLE users(
    id       INTEGER PRIMARY KEY AUTO_INCREMENT,
    login    VARCHAR(256),
    password CHAR(32)
);

CREATE TABLE statuses(
    id      INTEGER PRIMARY KEY AUTO_INCREMENT,
    user_id INTEGER,
    time    TIMESTAMP,
    status  VARCHAR(256),
    lat     DOUBLE,
    lng     DOUBLE,
    INDEX (time),
    INDEX (user_id),
    FOREIGN KEY (user_id)
        REFERENCES users(id)
);
