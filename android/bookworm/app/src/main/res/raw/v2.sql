create table if not exists book_chapter (
    book_id         TEXT,
    order_num       INTEGER,
    title           TEXT,
    entry           TEXT
);

create table if not exists book_binary (
    book_id         TEXT,
    content_type    TEXT,
    entry           BLOB
);

