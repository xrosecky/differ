
CREATE DATABASE differ;
GRANT ALL ON differ.* TO 'differ'@'localhost' IDENTIFIED BY 'differ';

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS images;

CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT,
    admin BOOLEAN,
    username VARCHAR(40),
    password_hash VARCHAR(256),
    password_salt VARCHAR(256),
    mail VARCHAR(40),
    PRIMARY KEY(id),
    UNIQUE KEY(username)
);

CREATE TABLE images (
    id INT NOT NULL AUTO_INCREMENT,
    filename VARCHAR(128),
    unique_name VARCHAR(128),
    owner_id INT,
    size INT,
    shared BOOLEAN,
    PRIMARY KEY(id)
);
