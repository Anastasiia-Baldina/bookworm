package org.vse.bookworm.repository.postgres;

import org.jetbrains.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.vse.bookworm.dao.Book;
import org.vse.bookworm.repository.BookRepository;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class PostgresBookRepository implements BookRepository {
    private static final String sqlListIds =
            "select " +
                    "   book_id" +
                    " from" +
                    "   book" +
                    " where" +
                    "   chat_id = :chat_id";
    private static final String sqlGetVersion =
            "select" +
                    "   book_version" +
                    " from" +
                    "   book" +
                    " where" +
                    "   book_id = :book_id";
    private static final String sqlGet =
            "select * from book where book_id = :book_id";
    private static final String sqlInsert =
            "insert into book" +
                    "(" +
                    "   book_id," +
                    "   chat_id," +
                    "   file," +
                    "   book_version" +
                    ")" +
                    " values" +
                    "(" +
                    "   :book_id," +
                    "   :chat_id," +
                    "   :file," +
                    "   :book_version" +
                    ")";
    private static final String sqlUpdate =
            "update book set" +
                    "   chat_id = :chat_id" +
                    "   file = :file" +
                    "   book_version = :book_version" +
                    " where" +
                    "   book_id = :book_id";
    private final NamedParameterJdbcTemplate jdbc;

    public PostgresBookRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void create(Book book) {
        var pSrc = new MapSqlParameterSource()
                .addValue("book_id", book.getId())
                .addValue("chat_id", book.getChatId())
                .addValue("file", book.getFile())
                .addValue("book_version", book.getVersion());
        jdbc.update(sqlInsert, pSrc);
    }

    @Override
    public void update(Book book) {
        var pSrc = new MapSqlParameterSource()
                .addValue("book_id", book.getId())
                .addValue("chat_id", book.getChatId())
                .addValue("file", book.getFile())
                .addValue("book_version", book.getVersion());
        jdbc.update(sqlUpdate, pSrc);
    }

    @Override
    public Collection<String> listIds(long chatId) {
        var pSrc = new MapSqlParameterSource("chat_id", chatId);
        return jdbc.queryForList(sqlListIds, pSrc, String.class);
    }

    @Nullable
    @Override
    public Book get(String bookId) {
        var pSrc = new MapSqlParameterSource("book_id", bookId);
        var res = jdbc.query(sqlGet, pSrc, BookRowMapper.INSTANCE);
        return res.isEmpty() ? null : res.getFirst();
    }

    @Nullable
    @Override
    public Integer getVersion(String bookId) {
        var pSrc = new MapSqlParameterSource("book_id", bookId);
        var res = jdbc.queryForList(sqlGetVersion, pSrc, Integer.class);
        return res.isEmpty() ? null : res.getFirst();
    }

    private static class BookRowMapper implements RowMapper<Book> {
        static final RowMapper<Book> INSTANCE = new BookRowMapper();

        @Override
        public Book mapRow(@Nonnull ResultSet rs, int rowNum) throws SQLException {
            return Book.builder()
                    .setId(rs.getString("book_id"))
                    .setChatId(rs.getLong("chat_id"))
                    .setVersion(rs.getInt("book_version"))
                    .setFile(rs.getBytes("file"))
                    .build();
        }
    }
}
