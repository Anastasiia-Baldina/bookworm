package ru.vse.bookworm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper INSTANCE;
    private static final String DB_NAME = "book_worm.db";
    private static final int SCHEMA_VERSION = 2;
    private final Context context;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseHelper(context.getApplicationContext());
        }
        return INSTANCE;
    }

    private DatabaseHelper(@NonNull Context context) {
        super(context.getApplicationContext(), DB_NAME, null, SCHEMA_VERSION);
        this.context = context.getApplicationContext();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        doUpgrade(db, 1, SCHEMA_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        doUpgrade(db, oldVersion + 1, newVersion);
    }

    private void doUpgrade(SQLiteDatabase db, int startV, int lastV) {
        for (int i = startV; i <= lastV; i++) {
            String script = loadScript(i);
            db.execSQL(script);
        }
    }

    public static void dropDatabase(@NonNull Context context) {
        context.getApplicationContext()
                .deleteDatabase(DB_NAME);
    }

    private String loadScript(int version) {
        int rawId = context.getResources()
                .getIdentifier("v" + version, "raw", context.getPackageName());
        var entry = new StringBuilder();
        try (var is = context.getResources().openRawResource(rawId);
             var reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                entry.append(line).append('\n');
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return entry.toString();
    }
}
