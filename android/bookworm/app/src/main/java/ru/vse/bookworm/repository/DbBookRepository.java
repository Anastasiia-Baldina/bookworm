package ru.vse.bookworm.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.vse.bookworm.book.Book;
import ru.vse.bookworm.book.Chapter;
import ru.vse.bookworm.db.DatabaseHelper;
import ru.vse.bookworm.book.BookInfo;

public class DbBookRepository implements BookRepository {
    private final DatabaseHelper dbHelper;
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
                    "   tg_group" +
                    " from" +
                    "   book" +
                    " where" +
                    "   deleted = 0" +
                    " order by" +
                    "   update_time desc";
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
                    "   deleted" +
                    ")" +
                    "values" +
                    "(?, ?, ?, ?, ?, ?, 0)";
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
    private static final String sqlGetChapter =
            "select" +
                    "   title," +
                    "   entry" +
                    " from " +
                    "   book_chapter" +
                    " where " +
                    "   book_id = ?" +
                    "   and order_num = ?";

    public DbBookRepository(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void saveBook(Book book) {
        var db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            var bookInfo = book.bookInfo();
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
    public void markAsDeleted(String id) {
        var db = dbHelper.getWritableDatabase();
        db.execSQL(sqlMarkAsDeleted, new Object[]{id});
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
                .build();
    }
}
