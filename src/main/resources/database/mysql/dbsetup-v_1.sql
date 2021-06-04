CREATE TABLE IF NOT EXISTS clans
(
    id   bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name varchar(64)        NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS rank
(
    id     int PRIMARY KEY NOT NULL,
    name   varchar(32)     NOT NULL UNIQUE,
    weight int             NOT NULL UNIQUE
);

INSERT INTO ranks(id, name, weight)
VALUES (1, 'Leader', 1);
INSERT INTO ranks(id, name, weight)
VALUES (2, 'Co-Leader', 2);
INSERT INTO ranks(id, name, weight)
VALUES (3, 'Member', 3);

CREATE TABLE IF NOT EXISTS players
(
    uuid    binary(16) PRIMARY KEY NOT NULL,
    name    varchar(16)            NOT NULL,
    clan_id bigint,
    rank_id int,
    FOREIGN KEY (rank_id) REFERENCES ranks (id)
);

CREATE TABLE IF NOT EXISTS smartclans_db_version
(
    version int PRIMARY KEY
);

INSERT INTO smartclans_db_version(version) VALUE (1);