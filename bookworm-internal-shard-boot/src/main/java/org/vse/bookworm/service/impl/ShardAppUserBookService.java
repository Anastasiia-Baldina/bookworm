package org.vse.bookworm.service.impl;

import org.vse.bookworm.dto.external.*;
import org.vse.bookworm.repository.SessionRepository;
import org.vse.bookworm.repository.UserBookRepository;
import org.vse.bookworm.service.AppUserBookService;

public class ShardAppUserBookService implements AppUserBookService {
    private final UserBookRepository userBookRepository;
    private final SessionRepository sessionRepository;

    public ShardAppUserBookService(UserBookRepository userBookRepository, SessionRepository sessionRepository) {
        this.userBookRepository = userBookRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public ListUserBookResponseDto listBooks(ListUserBookRequestDto rqDto) {
        var session = sessionRepository.get(rqDto.getSession());
        if(session == null || session.getUserId() != rqDto.getUserId()) {
            return new ListUserBookResponseDto()
                    .setErrorMessage("Сессия пользователя не найдена");
        }
        var books = userBookRepository.findByUserId(rqDto.getUserId()).stream()
                .map(x -> new UserBookDto()
                        .setBookId(x.getBookId())
                        .setUserId(x.getUserId())
                        .setBookVersion(x.getBookVersion())
                        .setUpdateTime(x.getUpdateTime().toEpochMilli())
                        .setChatName(x.getSource())
                        .setProgress(x.getProgress())
                        .setCurrentChapter(x.getCurrentChapter())
                        .setPosition(x.getCurrentPosition())
                        .setChatId(x.getChatId()))
                .toList();

        return new ListUserBookResponseDto()
                .setSuccess(true)
                .setUserBooks(books);
    }

    @Override
    public DeleteUserBookResponseDto deleteBook(DeleteUserBookRequestDto rqDto) {
        var session = sessionRepository.get(rqDto.getSession());
        if(session == null || session.getUserId() != rqDto.getUserId()) {
            return new DeleteUserBookResponseDto()
                    .setErrorMessage("Сессия пользователя не найдена");
        }
        userBookRepository.delete(rqDto.getUserId(), rqDto.getBookId());
        return new DeleteUserBookResponseDto()
                .setSuccess(true);
    }
}
