package org.vse.bookworm.service.impl;

import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import org.vse.bookworm.dao.Book;
import org.vse.bookworm.dao.Subscriber;
import org.vse.bookworm.dto.internal.BookSaveRequestDto;
import org.vse.bookworm.dto.internal.BookSaveResponseDto;
import org.vse.bookworm.repository.BookRepository;
import org.vse.bookworm.repository.SubscriberRepository;
import org.vse.bookworm.service.BookService;

import java.util.List;

public class ShardBookService implements BookService {
    private final BookRepository bookRepository;
    private final SubscriberRepository subscriberRepository;

    public ShardBookService(BookRepository bookRepository,
                            SubscriberRepository subscriberRepository) {
        this.bookRepository = bookRepository;
        this.subscriberRepository = subscriberRepository;
    }

    @Override
    public BookSaveResponseDto save(BookSaveRequestDto requestDto) {
        Integer bookV = bookRepository.getVersion(requestDto.getBookId());
        if(bookV == null) {
            bookRepository.create(fromRequest(requestDto));
        } else if(bookV != requestDto.getBookVersion()) {
            bookRepository.update(fromRequest(requestDto));
        }
        List<Long> userIds = subscriberRepository.findByChatId(requestDto.getChatId())
                .stream()
                .map(Subscriber::getUserId)
                .toList();
        return new BookSaveResponseDto()
                .setSuccess(true)
                .setUserIds(userIds);
    }

    private Book fromRequest(BookSaveRequestDto requestDto) {
        return Book.builder()
                .setChatId(requestDto.getChatId())
                .setId(requestDto.getBookId())
                .setFile(Base64.decode(requestDto.getFileBase64()))
                .setVersion(requestDto.getBookVersion())
                .build();
    }
}
