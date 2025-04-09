package org.vse.bookworm.repository.postgres;

import org.jetbrains.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.vse.bookworm.repository.SessionRepository;
import org.vse.bookworm.dao.Session;
import org.vse.bookworm.utils.DbUtils;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class PostgresSessionRepository implements SessionRepository {
    private static final String sqlInsert =
            "insert into user_session" +
                    "(" +
                    "   session_id," +
                    "   user_id," +
                    "   user_name," +
                    "   device_id," +
                    "   device_name," +
                    "   accept_code," +
                    "   create_time," +
                    "   try_count," +
                    "   update_time" +
                    ")" +
                    " values" +
                    "(" +
                    "   :session_id," +
                    "   :user_id," +
                    "   :user_name," +
                    "   :device_id," +
                    "   :device_name," +
                    "   :accept_code," +
                    "   :create_time," +
                    "   :try_count," +
                    "   :update_time" +
                    ")";
    private static final String sqlAttachDevice =
            "update user_session set" +
                    "   device_id = :device_id," +
                    "   device_name = :device_name" +
                    " where " +
                    "   session_id = :session_id" +
                    "   and accept_code = :accept_code";
    private static final String sqlRefresh =
            "update user_session set" +
                    "   update_time = :update_time" +
                    " where" +
                    "   session_id = :session_id";
    private static final String sqlDelete =
            "delete from user_session" +
                    " where" +
                    "   session_id = :session_id";
    private static final String getSqlDeleteUnused =
            "delete from user_session" +
                    " where" +
                    "   update_time is null" +
                    "   and user_id = :user_id";
    private static final String sqlFindByUserIdAndDeviceId =
            "select" +
                    "   *" +
                    " from" +
                    "   user_session" +
                    " where" +
                    "   user_id = :user_id" +
                    "   and device_id = :device_id";
    private static final String sqlFindByUserId =
            "select" +
                    "   *" +
                    " from" +
                    "   user_session" +
                    " where" +
                    "   user_id = :user_id";
    private static final String sqlGetById =
            "select" +
                    "   *" +
                    " from" +
                    "   user_session" +
                    " where" +
                    "   id = :id";
    private final NamedParameterJdbcTemplate jdbc;

    public PostgresSessionRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void create(Session session) {
        var pSrc = new MapSqlParameterSource()
                .addValue("session_id", session.getId())
                .addValue("user_id", session.getUserId())
                .addValue("user_name", session.getUsername())
                .addValue("device_id", session.getDeviceId())
                .addValue("device_name", session.getDeviceName())
                .addValue("accept_code", session.getAcceptCode())
                .addValue("create_time", DbUtils.timestamp(session.getCreateTime()))
                .addValue("try_count", session.getTryCount())
                .addValue("update_time", DbUtils.timestamp(session.getUpdateTime()));
        jdbc.update(getSqlDeleteUnused, pSrc);
        jdbc.update(sqlInsert, pSrc);
    }

    @Nonnull
    @Override
    public Collection<Session> find(long userId) {
        var pSrc = new MapSqlParameterSource("user_id", userId);
        return jdbc.query(sqlFindByUserId, pSrc, SessionRowMapper.INSTANCE);
    }

    @Nullable
    @Override
    public Session find(long userId, String deviceId) {
        var pSrc = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("device_id", deviceId);
        var res = jdbc.query(sqlFindByUserIdAndDeviceId, pSrc, SessionRowMapper.INSTANCE);
        return res.isEmpty() ? null : res.getFirst();
    }

    @Nullable
    @Override
    public Session get(String sessionId) {
        var pSrc = new MapSqlParameterSource("id", sessionId);
        var res = jdbc.query(sqlGetById, pSrc, SessionRowMapper.INSTANCE);
        return res.isEmpty() ? null : res.getFirst();
    }

    @Override
    public boolean delete(String sessionId) {
        return jdbc.update(sqlDelete, new MapSqlParameterSource("session_id", sessionId)) > 0;
    }

    @Override
    public boolean attachDevice(Session session) {
        var pSrc = new MapSqlParameterSource()
                .addValue("session_id", session.getId())
                .addValue("device_id", session.getDeviceId())
                .addValue("device_name", session.getDeviceName())
                .addValue("accept_code", session.getAcceptCode());
        return jdbc.update(sqlAttachDevice, pSrc) > 0;
    }

    @Override
    public boolean refresh(Session session) {
        var pSrc = new MapSqlParameterSource()
                .addValue("update_time", DbUtils.timestamp(session.getUpdateTime()))
                .addValue("session_id", session.getId());
        return jdbc.update(sqlRefresh, pSrc) > 0;
    }

    private static class SessionRowMapper implements RowMapper<Session> {
        final static RowMapper<Session> INSTANCE = new SessionRowMapper();

        @Override
        public Session mapRow(@Nonnull ResultSet rs, int rowNum) throws SQLException {
            return Session.builder()
                    .setId(rs.getString("session_id"))
                    .setUserId(rs.getLong("user_id"))
                    .setUsername(rs.getString("user_name"))
                    .setDeviceId(rs.getString("device_id"))
                    .setDeviceName(rs.getString("device_name"))
                    .setAcceptCode(rs.getInt("accept_code"))
                    .setCreateTime(DbUtils.instant(rs.getTimestamp("create_time")))
                    .setUpdateTime(DbUtils.instant(rs.getTimestamp("update_time")))
                    .setTryCount(rs.getInt("try_count"))
                    .build();
        }
    }
}
