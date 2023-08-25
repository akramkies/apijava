CREATE TABLE user (
    id Integer PRIMARY KEY AUTO_INCREMENT,
    username varchar(255) NOT NULL UNIQUE,
    password varchar(255) NOT NULL,
    role varchar(255),
    creation_date datetime,
    updated_date datetime
);
