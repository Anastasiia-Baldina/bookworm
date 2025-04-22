package org.vse.bookworm.repository.postgres;

import org.jetbrains.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.vse.bookworm.dao.UserBook;
import org.vse.bookworm.repository.UserBookRepository;
import org.vse.bookworm.utils.DbUtils;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class PostgresUserBookRepository implements UserBookRepository {
    private static final String sqlInsert =
            "insert into user_book" +
                    "(" +
                    "   user_id," +
                    "   book_id," +
                    "   chat_id," +
                    "   book_source," +
                    "   current_chapter," +
                    "   current_position," +
                    "   update_time," +
                    "   book_version," +
                    "   progress" +
                    ")" +
                    " values " +
                    "(" +
                    "   :user_id," +
                    "   :book_id," +
                    "   :chat_id," +
                    "   :book_source," +
                    "   :current_chapter," +
                    "   :current_position," +
                    "   :update_time," +
                    "   :book_version," +
                    "   :progress" +
                    ")";
    private static final String sqlUpdate =
            "update user_book set" +
                    "   current_chapter = :current_chapter," +
                    "   current_position = :current_position," +
                    "   update_time = :update_time," +
                    "   book_version = :book_version," +
                    "   progress = :progress" +
                    " where" +
                    "   user_id = :user_id" +
                    "   and book_id = :book_id";
    private static final String sqlFindByUserId =
            "select" +
                    "   *" +
                    " from" +
                    "   user_book" +
                    " where " +
                    "   user_id = :user_id";
    private static final String sqlGet =
            "select" +
                    "   *" +
                    " from" +
                    "   user_book" +
                    " where " +
                    "   user_id = :user_id" +
                    "   and book_id = :book_id";
    private static final String sqlDelete =
            "delete from user_book" +
                    " where " +
                    "   user_id = :user_id" +
                    "   and book_id = :book_id";
    private final NamedParameterJdbcTemplate jdbc;

    public PostgresUserBookRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void create(UserBook userBook) {
        var pSrc = new MapSqlParameterSource()
                .addValue("user_id", userBook.getUserId())
                .addValue("book_id", userBook.getBookId())
                .addValue("chat_id", userBook.getChatId())
                .addValue("book_source", userBook.getSource())
                .addValue("current_chapter", userBook.getCurrentChapter())
                .addValue("current_position", userBook.getCurrentPosition())
                .addValue("update_time", DbUtils.timestamp(userBook.getUpdateTime()))
                .addValue("book_version", userBook.getBookVersion())
                .addValue("progress", userBook.getProgress());
        jdbc.update(sqlInsert, pSrc);
    }

    @Override
    public void update(UserBook userBook) {
        var pSrc = new MapSqlParameterSource()
                .addValue("user_id", userBook.getUserId())
                .addValue("book_id", userBook.getBookId())
                .addValue("current_chapter", userBook.getCurrentChapter())
                .addValue("current_position", userBook.getCurrentPosition())
                .addValue("update_time", DbUtils.timestamp(userBook.getUpdateTime()))
                .addValue("book_version", userBook.getBookVersion())
                .addValue("progress", userBook.getProgress());
        jdbc.update(sqlUpdate, pSrc);
    }

    @Override
    public Collection<UserBook> findByUserId(long userId) {
        var pSrc = new MapSqlParameterSource("user_id", userId);
        return jdbc.query(sqlFindByUserId, pSrc, UserBookRowMapper.INSTANCE);
    }

    @Nullable
    @Override
    public UserBook get(long userId, String bookId) {
        var pSrc = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("book_id", bookId);
        var res = jdbc.query(sqlGet, pSrc, UserBookRowMapper.INSTANCE);
        return res.isEmpty() ? null : res.getFirst();
    }

    @Override
    public boolean delete(long userId, String bookId) {
        var pSrc = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("book_id", bookId);
        return jdbc.update(sqlDelete, pSrc) > 0;
    }

    private static class UserBookRowMapper implements RowMapper<UserBook> {
        static final RowMapper<UserBook> INSTANCE = new UserBookRowMapper();

        @Override
        public UserBook mapRow(@Nonnull ResultSet rs, int rowNum) throws SQLException {
            return UserBook.builder()
                    .setUserId(rs.getLong("user_id"))
                    .setBookId(rs.getString("book_id"))
                    .setChatId(rs.getLong("chat_id"))
                    .setSource(rs.getString("book_source"))
                    .setCurrentChapter(rs.getInt("current_chapter"))
                    .setCurrentPosition(rs.getDouble("current_position"))
                    .setUpdateTime(DbUtils.instant(rs.getTimestamp("update_time")))
                    .setBookVersion(rs.getInt("book_version"))
                    .setProgress(rs.getInt("progress"))
                    .build();
        }
    }
}
