package ru.vse.bookworm.utils;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ru.vse.bookworm.utils.dto.BookRequestDto;
import ru.vse.bookworm.utils.dto.BookResponseDto;
import ru.vse.bookworm.utils.dto.DeleteUserBookRequestDto;
import ru.vse.bookworm.utils.dto.DeleteUserBookResponseDto;
import ru.vse.bookworm.utils.dto.ListUserBookRequestDto;
import ru.vse.bookworm.utils.dto.ListUserBookResponseDto;
import ru.vse.bookworm.utils.dto.SignInRequestDto;
import ru.vse.bookworm.utils.dto.SignInResponseDto;

public class RestClient {
    private static final String endpoint = "http://192.168.0.83:8090/book-worm";
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static final RestClient INSTANCE = new RestClient();
    private final OkHttpClient httpClient = new OkHttpClient();

    private RestClient() {
    }

    public static RestClient instance() {
        return INSTANCE;
    }

    public CompletableFuture<SignInResponseDto> signIn(SignInRequestDto rqDto) {
        var url = endpoint + "/" + rqDto.getUserId() + "/sign-in";
        return asyncCall(rqDto, url, SignInResponseDto.class);
    }

    public CompletableFuture<ListUserBookResponseDto> listBooks(ListUserBookRequestDto rqDto) {
        var url = endpoint + "/" + rqDto.getUserId() + "/list-books";
        return asyncCall(rqDto, url, ListUserBookResponseDto.class);
    }

    public CompletableFuture<BookResponseDto> getBook(BookRequestDto rqDto, long chatId) {
        var url = endpoint + "/" + chatId + "/get-book";
        return asyncCall(rqDto, url, BookResponseDto.class);
    }

    public CompletableFuture<DeleteUserBookResponseDto> deleteBook(DeleteUserBookRequestDto rqDto) {
        var url = endpoint + "/" + 0 + "/delete-user-book";
        return asyncCall(rqDto, url, DeleteUserBookResponseDto.class);
    }

    private <R> CompletableFuture<R> asyncCall(Object request, String url, Class<R> rspType) {
        Request httpRq = new Request.Builder()
                .url(url)
                .post(RequestBody.create(Json.toJson(request), MEDIA_TYPE))
                .build();
        CompletableFuture<R> ftr = new CompletableFuture<>();
        Callback callback = new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ftr.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response httpRsp) throws IOException {
                if (httpRsp.isSuccessful() && httpRsp.body() != null) {
                    ftr.complete(Json.fromJson(httpRsp.body().string(), rspType));
                } else {
                    ftr.completeExceptionally(new IllegalStateException("Http code: " + httpRsp.code()));
                }
            }
        };
        httpClient.newCall(httpRq).enqueue(callback);
        return ftr;
    }
}
