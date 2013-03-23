CREATE TABLE users(
    id       INTEGER PRIMARY KEY AUTO_INCREMENT,
    login    VARCHAR(64) UNIQUE,
    password CHAR(32),
    email    VARCHAR(128),
    info     VARCHAR(128),
	isonline BOOL,
	reg_id   VARCHAR(256)
);

CREATE TABLE statuses(
    id      INTEGER PRIMARY KEY AUTO_INCREMENT,
    user_id INTEGER,
    status  VARCHAR(256),
    INDEX (user_id),
    FOREIGN KEY (user_id)
        REFERENCES users(id)
);

CREATE TABLE coordinates(
	id 		  INTEGER PRIMARY KEY AUTO_INCREMENT,
	user_id   INTEGER,
    status_id INTEGER,
	time      TIMESTAMP,
	lat       DOUBLE,
	lng       DOUBLE,
	INDEX (user_id),
	INDEX (status_id),
    INDEX (time),
	FOREIGN KEY (user_id)
    	REFERENCES users(id),
	FOREIGN KEY (status_id)
		REFERENCES statuses(id)
);
