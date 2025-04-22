package ru.vse.bookworm.repository.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import java.time.Instant;

import ru.vse.bookworm.repository.dao.UserSession;
import ru.vse.bookworm.db.DbHelper;
import ru.vse.bookworm.repository.SessionRepository;

public class DbSessionRepository implements SessionRepository {
    private static final String sqlDelete =
            "delete from user_session";
    private static final String sqlInsert =
            "insert into user_session" +
                    "(" +
                    "   session_id," +
                    "   update_time," +
                    "   user_id," +
                    "   device_id," +
                    "   device_name" +
                    ")" +
                    " values " +
                    "(?, ? ,?, ?, ?)";
    private static final String sqlGet =
            "select " +
                    "   session_id," +
                    "   update_time," +
                    "   user_id," +
                    "   device_id," +
                    "   device_name" +
                    " from " +
                    "   user_session";
    private final DbHelper dbHelper;

    public DbSessionRepository(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void save(UserSession session) {
        var db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            doDelete(db);
            var bindParams = new Object[]{
                    session.getSessionId(),
                    session.getUpdateTime().toEpochMilli(),
                    session.getUserId()
            };
            db.execSQL(sqlInsert, bindParams);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Nullable
    @Override
    public UserSession get() {
        var db = dbHelper.getReadableDatabase();
        try (var cursor = db.rawQuery(sqlGet, null)) {
            if (cursor.moveToNext()) {
                return mapUserSession(cursor);
            }
        }
        return null;
    }

    @Override
    public void delete() {
        doDelete(dbHelper.getWritableDatabase());
    }

    private void doDelete(SQLiteDatabase db) {
        db.execSQL(sqlDelete);
    }

    private static UserSession mapUserSession(Cursor cursor) {
        return UserSession.builder()
                .setSessionId(cursor.getString(0))
                .setUpdateTime(Instant.ofEpochMilli(cursor.getLong(1)))
                .setUserId(cursor.getLong(2))
                .setDeviceId(cursor.getString(3))
                .setDeviceName(cursor.getString(4))
                .build();
    }
}
