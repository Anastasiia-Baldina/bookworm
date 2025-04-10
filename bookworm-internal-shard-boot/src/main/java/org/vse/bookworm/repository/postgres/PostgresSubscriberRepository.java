package org.vse.bookworm.repository.postgres;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.vse.bookworm.dao.Subscriber;
import org.vse.bookworm.repository.SubscriberRepository;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class PostgresSubscriberRepository implements SubscriberRepository {
    private static final String sqlInsert =
            "insert into chat_subscriber" +
                    "   (chat_id, user_id, chat_name)" +
                    " values" +
                    "   (:chat_id, :user_id, :chat_name)";
    private static final String sqlDelete =
            "delete from chat_subscriber" +
                    " where" +
                    "   chat_id = :chat_id" +
                    "   and user_id = :user_id";
    private static final String sqlDeleteByChatName =
            "delete from chat_subscriber" +
                    " where" +
                    "   user_id = :user_id" +
                    "   and chat_name = :chat_name";
    private static final String sqlFindByUserId =
            "select" +
                    "   *" +
                    " from" +
                    "   chat_subscriber" +
                    " where" +
                    "   user_id = :user_id";
    private static final String sqlFindByChatId =
            "select" +
                    "   *" +
                    " from" +
                    "   chat_subscriber" +
                    " where" +
                    "   chat_id = :chat_id";
    private final NamedParameterJdbcTemplate jdbc;

    public PostgresSubscriberRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void create(Subscriber subscriber) {
        var pSrc = new MapSqlParameterSource()
                .addValue("chat_id", subscriber.getChatId())
                .addValue("user_id", subscriber.getUserId())
                .addValue("chat_name", subscriber.getChatName());
        jdbc.update(sqlInsert, pSrc);
    }

    @Override
    public boolean delete(Subscriber subscriber) {
        var pSrc = new MapSqlParameterSource()
                .addValue("chat_id", subscriber.getChatId())
                .addValue("user_id", subscriber.getUserId());
        return jdbc.update(sqlDelete, pSrc) > 0;
    }

    @Override
    public boolean delete(long userId, String chatName) {
        var pSrc = new MapSqlParameterSource()
                .addValue("chat_name", chatName)
                .addValue("user_id", userId);
        return jdbc.update(sqlDelete, pSrc) > 0;
    }

    @Override
    public Collection<Subscriber> findByUserId(long userId) {
        var pSrc = new MapSqlParameterSource("user_id", userId);
        return jdbc.query(sqlFindByUserId, pSrc, SubscriberRowMapper.INSTANCE);
    }

    @Override
    public Collection<Subscriber> findByChatId(long chatId) {
        var pSrc = new MapSqlParameterSource("chat_id", chatId);
        return jdbc.query(sqlFindByChatId, pSrc, SubscriberRowMapper.INSTANCE);
    }

    private static class SubscriberRowMapper implements RowMapper<Subscriber> {
        static final RowMapper<Subscriber> INSTANCE = new SubscriberRowMapper();

        @Override
        public Subscriber mapRow(@Nonnull ResultSet rs, int rowNum) throws SQLException {
            return Subscriber.builder()
                    .setChatId(rs.getLong("chat_id"))
                    .setUserId(rs.getLong("user_id"))
                    .setChatName(rs.getString("chat_name"))
                    .build();
        }
    }
}
