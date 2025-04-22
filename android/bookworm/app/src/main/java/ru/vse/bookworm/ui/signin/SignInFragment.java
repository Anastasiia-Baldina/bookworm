package ru.vse.bookworm.ui.signin;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

import ru.vse.bookworm.R;
import ru.vse.bookworm.book.BookInfo;
import ru.vse.bookworm.book.Fb2Parser;
import ru.vse.bookworm.db.DbHelper;
import ru.vse.bookworm.repository.dao.UserSession;
import ru.vse.bookworm.repository.sqlite.DbBookRepository;
import ru.vse.bookworm.repository.sqlite.DbSessionRepository;
import ru.vse.bookworm.utils.DeviceInfo;
import ru.vse.bookworm.utils.Gzip;
import ru.vse.bookworm.utils.RestClient;
import ru.vse.bookworm.utils.dto.BookRequestDto;
import ru.vse.bookworm.utils.dto.DeleteUserBookRequestDto;
import ru.vse.bookworm.utils.dto.ListUserBookRequestDto;
import ru.vse.bookworm.utils.dto.SignInRequestDto;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {
    private DbSessionRepository dbSessionRepository;
    private DbBookRepository dbBookRepository;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        var view = inflater.inflate(R.layout.fragment_signin, container, false);
        var context = view.getContext();
        var dbHelper = DbHelper.getInstance(context);

        dbSessionRepository = new DbSessionRepository(dbHelper);
        dbBookRepository = new DbBookRepository(dbHelper);

        var session = dbSessionRepository.get();
        if (session != null) {
            setSignedMode(view);
        } else {
            setSignInMode(view);
        }
        return view;
    }

    private void setSignedMode(View view) {
        EditText userIdEdit = view.findViewById(R.id.user_id);
        EditText acceptCodeEdit = view.findViewById(R.id.accept_code);
        Button doneBtn = view.findViewById(R.id.btn_done);
        Button signOutBtn = view.findViewById(R.id.btn_logout);
        TextView helpView = view.findViewById(R.id.signin_help);

        helpView.setVisibility(INVISIBLE);
        userIdEdit.setVisibility(INVISIBLE);
        acceptCodeEdit.setVisibility(INVISIBLE);
        doneBtn.setText("Синхронизировать");
        doneBtn.setOnClickListener(this::onSyncClicked);
        signOutBtn.setVisibility(VISIBLE);
        signOutBtn.setOnClickListener(this::onSignOutClicked);
    }

    private void setSignInMode(View view) {
        EditText userIdEdit = view.findViewById(R.id.user_id);
        EditText acceptCodeEdit = view.findViewById(R.id.accept_code);
        Button doneBtn = view.findViewById(R.id.btn_done);
        Button signOutBtn = view.findViewById(R.id.btn_logout);
        TextView helpView = view.findViewById(R.id.signin_help);

        userIdEdit.setVisibility(VISIBLE);
        acceptCodeEdit.setVisibility(VISIBLE);
        signOutBtn.setVisibility(INVISIBLE);
        doneBtn.setText("Войти");
        doneBtn.setOnClickListener(this::onSignInClicked);
        doneBtn.setEnabled(true);
        helpView.setVisibility(VISIBLE);
    }

    private void onSignOutClicked(View btnView) {
        View view = btnView.getRootView();
        dbSessionRepository.delete();
        setSignInMode(view);
    }

    private void onSignInClicked(View btnView) {
        View view = btnView.getRootView();
        EditText userIdEdit = view.findViewById(R.id.user_id);
        EditText acceptCodeEdit = view.findViewById(R.id.accept_code);
        TextView errorText = view.findViewById(R.id.signin_error);
        TextView helpView = view.findViewById(R.id.signin_help);
        Button doneBtn = view.findViewById(R.id.btn_done);
        Button signOutBtn = view.findViewById(R.id.btn_logout);

        errorText.setText("");
        long userId;
        try {
            userId = Long.parseLong(userIdEdit.getText().toString());
        } catch (Exception e) {
            errorText.setText("Заполните код пользователя");
            return;
        }
        int acceptCode;
        try {
            acceptCode = Integer.parseInt(acceptCodeEdit.getText().toString());
        } catch (Exception e) {
            errorText.setText("Заполните проверочный код");
            return;
        }
        btnView.setEnabled(false);

        var rqDto = new SignInRequestDto()
                .setAcceptCode(acceptCode)
                .setUserId(userId)
                .setDeviceId(DeviceInfo.instance().getDeviceId())
                .setDeviceName(DeviceInfo.instance().getDeviceName());

        RestClient.instance().signIn(rqDto)
                .whenComplete((rsp, th) -> {
                    if (th != null) {
                        errorText.post(() -> errorText.setText("Сервер не доступен. Попробуйте позже."));
                    } else if (!rsp.isSuccess()) {
                        errorText.post(() -> errorText.setText(rsp.getMessage()));
                    } else {
                        var session = UserSession.builder()
                                .setDeviceName(rqDto.getDeviceName())
                                .setDeviceId(rqDto.getDeviceId())
                                .setUserId(rqDto.getUserId())
                                .setSessionId(rsp.getSessionId())
                                .setUpdateTime(Instant.now())
                                .build();
                        dbSessionRepository.save(session);
                        helpView.post(() -> helpView.setVisibility(INVISIBLE));
                        userIdEdit.post(() -> userIdEdit.setVisibility(INVISIBLE));
                        acceptCodeEdit.post(() -> acceptCodeEdit.setVisibility(INVISIBLE));
                        doneBtn.post(() -> {
                            doneBtn.setText("Синхронизировать");
                            doneBtn.setOnClickListener(this::onSyncClicked);
                        });
                        signOutBtn.post(() -> signOutBtn.setVisibility(VISIBLE));
                    }
                    btnView.post(() -> btnView.setEnabled(true));
                });
    }

    private void onSyncClicked(View btnView) {
        btnView.setEnabled(false);
        View view = btnView.getRootView();
        var session = dbSessionRepository.get();
        if (session == null) {
            TextView errorText = view.findViewById(R.id.signin_error);
            setSignInMode(btnView);
            errorText.setText("Сессия пользователя не найдена");
            btnView.setEnabled(true);
            return;
        }
        TextView infoView = view.findViewById(R.id.signin_error);
        infoView.setText("");
        Map<String, BookInfo> storedBooks = dbBookRepository.list().stream()
                .collect(Collectors.toMap(BookInfo::id, x -> x));
        var deletedBooks = dbBookRepository.listMarked();
        int[] delCount = {0};
        for (var deletedBook : deletedBooks) {
            var bookId = deletedBook.id();
            var delRq = new DeleteUserBookRequestDto()
                    .setSession(session.getSessionId())
                    .setUserId(session.getUserId())
                    .setBookId(bookId);
            try {
                RestClient.instance().deleteBook(delRq).join();
                dbBookRepository.delete(bookId);
                delCount[0]++;
            } catch (Exception e) {
                Log.e("SYNC", "Delete book failed. id=" + bookId, e);
            }
        }
        var rqDto = new ListUserBookRequestDto()
                .setSession(session.getSessionId())
                .setUserId(session.getUserId());

        RestClient.instance().listBooks(rqDto)
                .whenComplete((rsp, th) -> {
                    if (th != null) {
                        infoView.post(() -> infoView.setText("Сервер не доступен. Попробуйте позже."));
                    } else if (!rsp.isSuccess()) {
                        infoView.post(() -> infoView.setText(rsp.getErrorMessage()));
                    } else {
                        infoView.post(() -> infoView.setText("Обновление данных..."));
                        int loadCount = 0;
                        int updCount = 0;
                        var remoteBooks = rsp.getUserBooks();
                        for (var remoteBook : remoteBooks) {
                            var bookId = remoteBook.getBookId();
                            var storedBook = storedBooks.get(bookId);
                            if (storedBook == null || storedBook.version() != remoteBook.getBookVersion()) {
                                var bookRq = new BookRequestDto()
                                        .setBookId(bookId)
                                        .setSession(session.getSessionId())
                                        .setUserId(session.getUserId());
                                try {
                                    var bookRsp = RestClient.instance().getBook(bookRq, remoteBook.getChatId()).join();
                                    if (!bookRsp.isSuccess()) {
                                        continue;
                                    }
                                    var bookEntry = Gzip.decompressFromBase64(bookRsp.getBase64Entry());
                                    var book = Fb2Parser.instance().parse(new ByteArrayInputStream(bookEntry));
                                    book.setId(bookId)
                                            .setVersion(remoteBook.getBookVersion())
                                            .setTgGroup(remoteBook.getChatName())
                                            .setChatId(remoteBook.getChatId());
                                    if (storedBook == null
                                            || storedBook.updateTime() == null
                                            || storedBook.updateTime().toEpochMilli() < remoteBook.getUpdateTime()) {
                                        book.setProgress(remoteBook.getProgress())
                                                .setCurrentChapter(remoteBook.getCurrentChapter());
                                        loadCount++;
                                    } else {
                                        book.setProgress(storedBook.progress())
                                                .setCurrentChapter(storedBook.currentChapter());
                                        updCount++;
                                    }
                                    dbBookRepository.saveBook(book.build());
                                } catch (Exception e) {
                                    Log.e("SYNC", "Update book failed. id=" + bookId, e);
                                }
                            }
                        }
                        String stats = String.format("Новых: %s. Обновлено: %s. Удалено %s", loadCount, updCount, delCount[0]);
                        infoView.post(() -> infoView.setText(stats));
                    }
                    btnView.post(() -> btnView.setEnabled(true));
                });
    }
}