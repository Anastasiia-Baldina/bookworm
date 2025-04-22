create table if not exists book_binary (
    book_id         TEXT,
    content_type    TEXT,
    entry           BLOB
);