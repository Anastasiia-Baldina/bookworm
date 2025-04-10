package org.vse.bookworm.repository.postgres;

import org.jetbrains.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.vse.bookworm.dao.Chat;
import org.vse.bookworm.repository.ChatRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgresChatRepository implements ChatRepository {
    private static final String sqlGet = "select * from chat where chat_id = :chat_id";
    private static final String sqlInsert =
            "insert into chat" +
                    "(" +
                    "   chat_id," +
                    "   chat_name" +
                    ")" +
                    " values" +
                    "(" +
                    "   :chat_id," +
                    "   :chat_name" +
                    ")";
    private static final String sqlFind = "select * from chat where chat_name = :chat_name";
    private static final String sqlUpdate = "update chat set" +
            "   chat_name = :chat_name" +
            " where" +
            "   chat_id = :chat_id";
    private final NamedParameterJdbcTemplate jdbc;

    public PostgresChatRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Nullable
    @Override
    public Chat get(long chat_id) {
        var pSrc = new MapSqlParameterSource("chat_id", chat_id);
        var res = jdbc.query(sqlGet, pSrc, ChatRowMapper.INSTANCE);
        return res.isEmpty() ? null : res.getFirst();
    }

    @Override
    public void save(Chat chat) {
        var sql = get(chat.getId()) != null ? sqlUpdate : sqlInsert;
        var pSrc = new MapSqlParameterSource()
                .addValue("chat_id", chat.getId())
                .addValue("chat_name", chat.getName());
        jdbc.update(sql, pSrc);
    }

    @Nullable
    @Override
    public Chat findByName(String chatName) {
        var pSrc = new MapSqlParameterSource("chat_name", chatName);
        var res = jdbc.query(sqlFind, pSrc, ChatRowMapper.INSTANCE);
        return res.isEmpty() ? null : res.getFirst();
    }

    private static class ChatRowMapper implements RowMapper<Chat> {
        static RowMapper<Chat> INSTANCE = new ChatRowMapper();

        @Override
        public Chat mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Chat(
                    rs.getLong("chat_id"),
                    rs.getString("chat_name")
            );
        }
    }
}
