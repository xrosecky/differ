CREATE TABLE users (
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    admin BOOLEAN,
    username VARCHAR(40),
    password_hash VARCHAR(256),
    password_salt VARCHAR(256),
    mail VARCHAR(40),
    PRIMARY KEY(id)
);

CREATE TABLE images (
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    filename VARCHAR(128),
    unique_name VARCHAR(128),
    owner_id INT,
    size INT,
    shared BOOLEAN,
    PRIMARY KEY(id)
)
