create table if not exists user_session (
    session_id      TEXT,
    user_id         INTEGER,
    update_time     INTEGER,
    device_id       TEXT,
    device_name     TEXT
);