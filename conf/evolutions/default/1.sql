# --- !Ups

CREATE TABLE "KEY_VALUE" (
    "KEY" VARCHAR NOT NULL PRIMARY KEY,
    "VALUE" VARCHAR
);

# --- !Downs

DROP TABLE "KEY_VALUE";
