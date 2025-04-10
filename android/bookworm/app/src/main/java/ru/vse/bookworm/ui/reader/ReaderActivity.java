package ru.vse.bookworm.ui.reader;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ru.vse.bookworm.MainActivity;
import ru.vse.bookworm.R;
import ru.vse.bookworm.book.Chapter;
import ru.vse.bookworm.db.DatabaseHelper;
import ru.vse.bookworm.repository.BookRepository;
import ru.vse.bookworm.repository.DbBookRepository;
import ru.vse.bookworm.book.BookInfo;
import ru.vse.bookworm.utils.Json;

public class ReaderActivity extends AppCompatActivity {
    private TextView bookTitle;
    private ReaderWebView bookView;
    private BookRepository repository;
    private BookInfo bookInfo;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        Bundle state = savedState == null ? getIntent().getExtras() : savedState;

        repository = new DbBookRepository(DatabaseHelper.getInstance(this));
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_reader);
        var rootView = findViewById(android.R.id.content).getRootView();
        bookTitle = rootView.findViewById(R.id.book_title);
        bookView = rootView.findViewById(R.id.book_view);
        bookView.setGestureDetector(new GestureDetector(this, new ReaderGestureDetectorListener()));
        var btnBack = rootView.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(view -> {
            var intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        if (state != null) {
            bookInfo = Json.fromJson(state.getString("bookInfo"), BookInfo.class);
            if (bookInfo != null) {
                bookTitle.setText(bookInfo.title());
                loadBook(bookInfo.id());
            }
        }
    }

    private void loadBook(String bookId) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        int order = 0;
        Chapter chapter;
        while ((chapter = repository.getChapter(bookId, order)) != null) {
            sb.append(chapter.title())
                    .append(chapter.text());
            order++;
        }
        sb.append("</body></html>");
        bookView.loadData(sb.toString(), "text/html", null);
    }
}
