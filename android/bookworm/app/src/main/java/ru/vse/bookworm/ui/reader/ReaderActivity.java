package ru.vse.bookworm.ui.reader;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import ru.vse.bookworm.R;
import ru.vse.bookworm.book.Chapter;
import ru.vse.bookworm.db.DbHelper;
import ru.vse.bookworm.repository.BookRepository;
import ru.vse.bookworm.repository.sqlite.DbBookRepository;
import ru.vse.bookworm.book.BookInfo;
import ru.vse.bookworm.utils.Json;

public class ReaderActivity extends AppCompatActivity {
    private ReaderWebView bookView;
    private BookRepository repository;
    private BookInfo bookInfo;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        Bundle state = savedState == null ? getIntent().getExtras() : savedState;
        repository = new DbBookRepository(DbHelper.getInstance(this));
        setContentView(R.layout.activity_reader);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        var rootView = findViewById(android.R.id.content).getRootView();
        bookView = rootView.findViewById(R.id.book_view);
        bookView.setGestureDetector(new GestureDetector(this, new ReaderGestureDetectorListener()));
        if (state != null) {
            bookInfo = Json.fromJson(state.getString("bookInfo"), BookInfo.class);
            if (bookInfo != null) {
                if (supportActionBar != null) {
                    supportActionBar.setTitle(bookInfo.title());
                }
                loadBook(bookInfo.id());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            saveProgress();
            finish();
        }
        return true;
    }

    private void loadBook(String bookId) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head>");
        sb.append("<style type=\"text/css\">\n");
        sb.append(".stanza { padding-left: 1em; }");
        sb.append("</style\n");
        sb.append("</head><body>");
        int order = 0;
        Chapter chapter;
        while ((chapter = repository.getChapter(bookId, order)) != null) {
            sb.append(chapter.title())
                    .append(chapter.text());
            order++;
        }
        sb.append("</body></html>");
        bookView.loadData(sb.toString(), "text/html", null);
        bookView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                restoreProgress(view);
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        saveProgress();
    }

    private void restoreProgress(WebView view) {
        int contentHeight = view.getContentHeight();
        int y = (int) ((double) contentHeight * bookInfo.position());
        view.scrollTo(0, y);
    }

    private void saveProgress() {
        int contentHeight = bookView.getContentHeight();
        int viewHeight = bookView.getHeight();
        int scrollY = bookView.getScrollY();

        int newProgress = Math.min(((scrollY + viewHeight) * 100 / contentHeight), 100);
        double position = ((double) scrollY / ((double) contentHeight));
        bookInfo = bookInfo.toBuilder()
                .setProgress(newProgress)
                .setPosition(position)
                .build();
        repository.saveProgress(bookInfo);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveProgress();
    }
}
