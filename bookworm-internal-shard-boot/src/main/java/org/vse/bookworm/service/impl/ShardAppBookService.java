package org.vse.bookworm.service.impl;

import org.vse.bookworm.dto.external.BookRequestDto;
import org.vse.bookworm.dto.external.BookResponseDto;
import org.vse.bookworm.repository.BookRepository;
import org.vse.bookworm.service.AppBookService;

import java.util.Base64;

public class ShardAppBookService implements AppBookService {
    private final BookRepository bookRepository;

    public ShardAppBookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public BookResponseDto getBook(BookRequestDto rqDto) {
        var book = bookRepository.get(rqDto.getBookId());
        if(book == null) {
            return new BookResponseDto()
                    .setErrorMessage("Книга не найдена");
        }
        return new BookResponseDto()
                .setSuccess(true)
                .setBase64Entry(Base64.getEncoder().encodeToString(book.getFile()));
    }
}
