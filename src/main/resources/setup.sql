CREATE TABLE IF NOT EXISTS Tags
(
    uuid    CHAR(36) NOT NULL,
    name    varchar(256) NOT NULL,
    symbol  varchar(10) NOT NULL,
    PRIMARY KEY (uuid)
);