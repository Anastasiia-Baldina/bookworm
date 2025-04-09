package org.vse.bookworm.repository.postgres;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.vse.bookworm.dao.ChatMessage;
import org.vse.bookworm.dao.Message;
import org.vse.bookworm.repository.ChatMessageRepository;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class PostgresChatMsgRepository implements ChatMessageRepository {
    private static final String sqlInsert =
            "insert into chat_message" +
                    "(" +
                    "   chat_id," +
                    "   username" +
                    "   message_id," +
                    "   message," +
                    "   category" +
                    ")" +
                    " values" +
                    "(" +
                    "   :chat_id," +
                    "   :username" +
                    "   :message_id," +
                    "   :message," +
                    "   :category" +
                    ")";
    private static final String sqlList =
            "select" +
                    "   *" +
                    " from" +
                    "   chat_massage" +
                    " where" +
                    "   category = :category";
    private static final String sqlDelete =
            "delete" +
                    " from" +
                    "   chat_message" +
                    " where" +
                    "   chat_id = :chat_id" +
                    "   message_id = :message_id";
    private final NamedParameterJdbcTemplate jdbc;

    public PostgresChatMsgRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void insert(ChatMessage chatMsg) {
        var pSrc = new MapSqlParameterSource()
                .addValue("chat_id", chatMsg.getChatId())
                .addValue("message_id", chatMsg.getMessageId())
                .addValue("category", chatMsg.getCategory())
                .addValue("message", chatMsg.getMessage().getJson())
                .addValue("username", chatMsg.getUsername());
        jdbc.update(sqlInsert, pSrc);
    }

    @Override
    public void delete(long chatId, long messageId) {
        var pSrc = new MapSqlParameterSource()
                .addValue("chat_id", chatId)
                .addValue("message_id", messageId);
        jdbc.update(sqlDelete, pSrc);
    }

    @Override
    public Collection<ChatMessage> list(long chatId, String category) {
        var pSrc = new MapSqlParameterSource()
                .addValue("chat_id", chatId)
                .addValue("category", category);
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
                    .setMessage(Message.fromJson(rs.getString("message")))
                    .build();
        }
    }
}
