-- liquibase formatted sql

-- changeset sbukaevsky:1
CREATE TABLE notification_task
(
    primary_key   SERIAL,
    chat_id       INTEGER,
    team_name     TEXT,
    text_message  TEXT,
    date_and_time TIMESTAMP
)

-- changeset sbukaevsky:2
ALTER TABLE notification_task DROP COLUMN team_name;