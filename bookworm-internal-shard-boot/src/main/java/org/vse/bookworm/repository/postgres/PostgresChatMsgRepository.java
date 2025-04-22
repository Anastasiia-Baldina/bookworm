package org.vse.bookworm.repository.postgres;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.vse.bookworm.dao.ChatMessage;
import org.vse.bookworm.dao.Message;
import org.vse.bookworm.repository.ChatMessageRepository;
import org.vse.bookworm.utils.Json;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class PostgresChatMsgRepository implements ChatMessageRepository {
    private static final String sqlInsert =
            "insert into chat_message" +
                    "(" +
                    "   chat_id," +
                    "   username," +
                    "   message_id," +
                    "   message," +
                    "   category" +
                    ")" +
                    " values" +
                    "(" +
                    "   :chat_id," +
                    "   :username," +
                    "   :message_id," +
                    "   :message::jsonb," +
                    "   :category" +
                    ")";
    private static final String sqlUpdate =
            "update chat_message set" +
                    "   message = :message::jsonb," +
                    "   category = :category," +
                    "   username = :username" +
                    " where" +
                    "   chat_id = :chat_id" +
                    "   and message_id = :message_id";
    private static final String sqlGet =
            "select" +
                    "    *" +
                    " from " +
                    "   chat_message" +
                    " where" +
                    "   chat_id = :chat_id" +
                    "   and message_id = :message_id";
    private static final String sqlList =
            "select" +
                    "   *" +
                    " from" +
                    "   chat_message" +
                    " where" +
                    "   category = :category" +
                    "   and chat_id = :chat_id" +
                    "   and message_id >= :message_id" +
                    " order by" +
                    "   message_id" +
                    " limit" +
                    "   :limit";
    private static final String sqlDelete =
            "delete" +
                    " from" +
                    "   chat_message" +
                    " where" +
                    "   chat_id = :chat_id" +
                    "   and message_id = :message_id";
    private final NamedParameterJdbcTemplate jdbc;

    public PostgresChatMsgRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void save(ChatMessage chatMsg) {
        var pSrc = new MapSqlParameterSource()
                .addValue("chat_id", chatMsg.getChatId())
                .addValue("message_id", chatMsg.getMessageId())
                .addValue("category", chatMsg.getCategory())
                .addValue("message", Json.toJson(chatMsg.getMessage()))
                .addValue("username", chatMsg.getUsername());
        var sql = jdbc.query(sqlGet, pSrc,ChatMsgRowMapper.INSTANCE).isEmpty()
                ? sqlInsert
                : sqlUpdate;
        jdbc.update(sql, pSrc);
    }

    @Override
    public void delete(long chatId, long messageId) {
        var pSrc = new MapSqlParameterSource()
                .addValue("chat_id", chatId)
                .addValue("message_id", messageId);
        jdbc.update(sqlDelete, pSrc);
    }

    @Override
    public Collection<ChatMessage> list(long chatId,
                                        String category,
                                        long messageId,
                                        int limit) {
        var pSrc = new MapSqlParameterSource()
                .addValue("chat_id", chatId)
                .addValue("category", category)
                .addValue("message_id", messageId)
                .addValue("limit", limit);
        return jdbc.query(sqlList, pSrc, ChatMsgRowMapper.INSTANCE);
    }

    private static class ChatMsgRowMapper implements RowMapper<ChatMessage> {
        static final RowMapper<ChatMessage> INSTANCE = new ChatMsgRowMapper();

        @Override
        public ChatMessage mapRow(@Nonnull ResultSet rs, int rowNum) throws SQLException {
            return ChatMessage.builder()
                    .setChatId(rs.getLong("chat_id"))
                    .setCategory(rs.getString("category"))
                    .setUsername(rs.getString("username"))
                    .setMessageId(rs.getLong("message_id"))
                    .setMessage(Json.fromJson(rs.getString("message"), Message.class))
                    .build();
        }
    }
}
