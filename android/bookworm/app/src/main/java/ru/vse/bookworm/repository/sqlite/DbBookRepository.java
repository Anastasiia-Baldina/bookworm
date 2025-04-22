package ru.vse.bookworm.repository.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.vse.bookworm.book.Book;
import ru.vse.bookworm.book.Chapter;
import ru.vse.bookworm.db.DbHelper;
import ru.vse.bookworm.book.BookInfo;
import ru.vse.bookworm.repository.BookRepository;

public class DbBookRepository implements BookRepository {
    private static final String sqlGetCount =
            "select count(*)" +
                    " from " +
                    "   book" +
                    " where" +
                    "   deleted = 0";
    private static final String sqlList =
            "select" +
                    "   id," +
                    "   title," +
                    "   author," +
                    "   progress," +
                    "   tg_group," +
                    "   update_time," +
                    "   version," +
                    "   position," +
                    "   chat_id" +
                    " from" +
                    "   book" +
                    " where" +
                    "   deleted = 0" +
                    " order by" +
                    "   update_time desc";
    private static final String sqlGet =
            "select" +
                    "   id," +
                    "   title," +
                    "   author," +
                    "   progress," +
                    "   tg_group," +
                    "   update_time," +
                    "   version," +
                    "   position," +
                    "   chat_id" +
                    " from" +
                    "   book" +
                    " where" +
                    "   id  = ?";
    private static final String sqlListMarked =
            "select" +
                    "   id," +
                    "   title," +
                    "   author," +
                    "   progress," +
                    "   tg_group," +
                    "   update_time," +
                    "   version," +
                    "   position," +
                    "   chat_id" +
                    " from" +
                    "   book" +
                    " where" +
                    "   deleted = 1";
    private static final String sqlMarkAsDeleted =
            "update book set" +
                    "   deleted = 1" +
                    " where" +
                    "   id = ?";
    private static final String sqlInsertInfo =
            "insert into book " +
                    "( " +
                    "   id," +
                    "   title," +
                    "   author," +
                    "   progress," +
                    "   tg_group," +
                    "   update_time," +
                    "   deleted," +
                    "   version," +
                    "   position," +
                    "   chat_id" +
                    ")" +
                    "values" +
                    "(?, ?, ?, ?, ?, ?, 0, ?, ?, ?)";
    private static final String sqlInsertChapter =
            "insert into book_chapter" +
                    "(" +
                    "   book_id," +
                    "   order_num," +
                    "   title," +
                    "   entry" +
                    ")" +
                    "values" +
                    "(?, ?, ?, ?)";
    private static final String sqlDeleteChapters =
            "delete from book_chapter" +
                    " where" +
                    "   book_id = ?";
    private static final String sqlDeleteInfo =
            "delete from book" +
                    " where" +
                    "   id = ?";
    private static final String sqlGetChapter =
            "select" +
                    "   title," +
                    "   entry" +
                    " from " +
                    "   book_chapter" +
                    " where " +
                    "   book_id = ?" +
                    "   and order_num = ?";
    private static final String sqlSaveProgress =
            "update book set" +
                    "   progress = ?," +
                    "   position = ?" +
                    " where" +
                    "   id = ?";
    private final DbHelper dbHelper;

    public DbBookRepository(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void saveBook(Book book) {
        var db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            var bookInfo = book.bookInfo();
            db.execSQL(sqlDeleteChapters, new Object[]{bookInfo.id()});
            db.execSQL(sqlDeleteInfo, new Object[]{bookInfo.id()});
            doSaveInfo(bookInfo, db);
            doSaveChapters(bookInfo.id(), book.chapters(), db);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public Chapter getChapter(String bookId, int order) {
        var db = dbHelper.getReadableDatabase();
        var bindParams = new String[]{bookId, String.valueOf(order)};
        try (var cursor = db.rawQuery(sqlGetChapter, bindParams)) {
            if (cursor.moveToNext()) {
                return Chapter.builder()
                        .setOrder(order)
                        .appendTitle(cursor.getString(0))
                        .appendText(cursor.getString(1))
                        .build();
            }
        }
        return null;
    }

    private void doSaveInfo(BookInfo bookInfo, SQLiteDatabase db) {
        var bindParams = new Object[]{
                bookInfo.id(),
                bookInfo.title(),
                bookInfo.author(),
                bookInfo.progress(),
                bookInfo.telegramGroup(),
                bookInfo.updateTime().toEpochMilli(),
                bookInfo.version(),
                bookInfo.position(),
                bookInfo.chatId()
        };
        db.execSQL(sqlInsertInfo, bindParams);
    }

    private void doSaveChapters(String bookId, List<Chapter> chapters, SQLiteDatabase db) {
        for (int i = 0; i < chapters.size(); i++) {
            var cpt = chapters.get(i);
            var bindParams = new Object[]{
                    bookId, cpt.order(), cpt.title(), cpt.text()
            };
            db.execSQL(sqlInsertChapter, bindParams);
        }
    }

    @Override
    public List<BookInfo> list() {
        var db = dbHelper.getReadableDatabase();
        try (var cursor = db.rawQuery(sqlList, null)) {
            var count = cursor.getCount();
            var res = new ArrayList<BookInfo>(count);
            while (cursor.moveToNext()) {
                res.add(mapBookInfoCursorRow(cursor));
            }
            return res;
        }
    }

    @Override
    public BookInfo get(String bookId) {
        var db = dbHelper.getReadableDatabase();
        try (var cursor = db.rawQuery(sqlGet, new String[]{bookId})) {
            if (cursor.moveToNext()) {
                return mapBookInfoCursorRow(cursor);
            }
        }
        return null;
    }

    @Override
    public void markAsDeleted(String bookId) {
        var db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            var bindParams = new Object[]{bookId};
            db.execSQL(sqlMarkAsDeleted, bindParams);
            db.execSQL(sqlDeleteChapters, bindParams);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void delete(String bookId) {
        var db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL(sqlDeleteChapters, new Object[]{bookId});
            db.execSQL(sqlDeleteInfo, new Object[]{bookId});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void saveProgress(BookInfo bookInfo) {
        var db = dbHelper.getWritableDatabase();
        var bindParams = new Object[]{
                bookInfo.progress(),
                bookInfo.position(),
                bookInfo.id()
        };
        db.execSQL(sqlSaveProgress, bindParams);
    }

    @Override
    public List<BookInfo> listMarked() {
        var db = dbHelper.getReadableDatabase();
        try (var cursor = db.rawQuery(sqlListMarked, null)) {
            var count = cursor.getCount();
            var res = new ArrayList<BookInfo>(count);
            while (cursor.moveToNext()) {
                res.add(mapBookInfoCursorRow(cursor));
            }
            return res;
        }
    }

    @Override
    public int getCount() {
        var db = dbHelper.getReadableDatabase();
        try (var cursor = db.rawQuery(sqlGetCount, null)) {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
    }

    private static BookInfo mapBookInfoCursorRow(Cursor cursor) {
        return BookInfo.builder()
                .setId(cursor.getString(0))
                .setTitle(cursor.getString(1))
                .setAuthor(cursor.getString(2))
                .setProgress(cursor.getInt(3))
                .setTgGroup(cursor.getString(4))
                .setUpdateTime(Instant.ofEpochMilli(cursor.getLong(5)))
                .setVersion(cursor.getInt(6))
                .setPosition(cursor.getDouble(7))
                .setChatId(cursor.getLong(8))
                .build();
    }
}
