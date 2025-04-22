create table if not exists book (
    id                  TEXT,
    title               TEXT,
    author              TEXT,
    tg_group            TEXT,
    progress            INTEGER,
    update_time         INTEGER,
    deleted             INTEGER,
    version             INTEGER,
    chat_id             INTEGER,
    position            REAL
);