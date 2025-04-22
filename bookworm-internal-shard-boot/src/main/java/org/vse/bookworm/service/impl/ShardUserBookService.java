package org.vse.bookworm.service.impl;

import org.vse.bookworm.dao.UserBook;
import org.vse.bookworm.dto.internal.UserBookSaveRequestDto;
import org.vse.bookworm.dto.internal.UserBookSaveResponseDto;
import org.vse.bookworm.repository.UserBookRepository;
import org.vse.bookworm.service.UserBookService;

import java.time.Instant;

public class ShardUserBookService implements UserBookService {
    private final UserBookRepository userBookRepository;

    public ShardUserBookService(UserBookRepository userBookRepository) {
        this.userBookRepository = userBookRepository;
    }

    @Override
    public UserBookSaveResponseDto save(UserBookSaveRequestDto requestDto) {
        var prev = userBookRepository.get(requestDto.getUserId(), requestDto.getBookId());
        if (prev == null) {
            userBookRepository.create(toUserBook(requestDto));
        } else if (prev.getUpdateTime().isBefore(Instant.ofEpochMilli(requestDto.getUpdateTime()))) {
            userBookRepository.update(toUserBook(requestDto, prev));
        }
        return new UserBookSaveResponseDto()
                .setSuccess(true);
    }

    private static UserBook toUserBook(UserBookSaveRequestDto requestDto) {
        return UserBook.builder()
                .setBookId(requestDto.getBookId())
                .setUserId(requestDto.getUserId())
                .setChatId(requestDto.getChatId())
                .setUpdateTime(Instant.ofEpochMilli(requestDto.getUpdateTime()))
                .setProgress(requestDto.getProgress())
                .setCurrentChapter(requestDto.getCurrentChapter())
                .setCurrentPosition(requestDto.getPosition())
                .setBookVersion(requestDto.getBookVersion())
                .setSource(requestDto.getChatName())
                .build();
    }

    private static UserBook toUserBook(UserBookSaveRequestDto requestDto, UserBook prev) {
        return UserBook.builder()
                .setBookId(requestDto.getBookId())
                .setUserId(requestDto.getUserId())
                .setChatId(requestDto.getChatId())
                .setUpdateTime(Instant.ofEpochMilli(requestDto.getUpdateTime()))
                .setProgress(prev.getProgress())
                .setCurrentChapter(prev.getCurrentChapter())
                .setCurrentPosition(prev.getCurrentPosition())
                .setBookVersion(requestDto.getBookVersion())
                .setSource(requestDto.getChatName())
                .build();
    }
}
