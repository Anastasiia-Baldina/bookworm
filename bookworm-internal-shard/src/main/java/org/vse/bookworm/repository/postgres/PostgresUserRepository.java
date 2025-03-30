package org.vse.bookworm.repository.postgres;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.vse.bookworm.repository.UserRepository;
import org.vse.bookworm.repository.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgresUserRepository implements UserRepository {
    private static final String sqlInsertUser =
            "insert" +
                    " into user" +
                    " (" +
                    "   id," +
                    "   first_name," +
                    "   last_name," +
                    "   username" +
                    " )" +
                    " values" +
                    " (" +
                    "   :id," +
                    "   :first_name," +
                    "   :last_name," +
                    "   :username" +
                    " )";
    private static final String sqlSelectById =
            "select" +
                    "   id," +
                    "   first_name," +
                    "   last_name," +
                    "   username" +
                    " from user" +
                    " where" +
                    "   id = :id";
    private static final String sqlSelectByUserName =
            "select" +
                    "   id," +
                    "   first_name," +
                    "   last_name," +
                    "   username" +
                    " from user" +
                    " where" +
                    "   username = :username";
    private final NamedParameterJdbcTemplate jdbc;

    public PostgresUserRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        jdbc = namedParameterJdbcTemplate;
    }

    @Override
    public void create(User user) {
        jdbc.update(sqlInsertUser, bind(user));
    }

    @Override
    @Nullable
    public User get(long id) {
        var res = jdbc.query(sqlSelectById, bind(id), UserRowMapper.INSTANCE);
        return res.isEmpty() ? null : res.getFirst();
    }

    @Nullable
    @Override
    public User get(String username) {
        var res = jdbc.query(sqlSelectByUserName, bind(username), UserRowMapper.INSTANCE);
        return res.isEmpty() ? null : res.getFirst();
    }

    private static SqlParameterSource bind(User user) {
        return new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("first_name", user.getFirstName())
                .addValue("last_name", user.getLastName())
                .addValue("username", user.getUserName());
    }

    private static SqlParameterSource bind(long userId) {
        return new MapSqlParameterSource("id", userId);
    }

    private static SqlParameterSource bind(String username) {
        return new MapSqlParameterSource("user_name", username);
    }

    private static class UserRowMapper implements RowMapper<User> {
        static UserRowMapper INSTANCE = new UserRowMapper();

        @Override
        public User mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            return User.builder()
                    .setId(rs.getLong("id"))
                    .setFirstName(rs.getString("first_name"))
                    .setLastName(rs.getString("last_name"))
                    .setUserName(rs.getString("username"))
                    .build();
        }
    }
}
