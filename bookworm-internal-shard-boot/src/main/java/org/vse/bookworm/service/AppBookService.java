package org.vse.bookworm.service;

import org.vse.bookworm.dto.external.BookRequestDto;
import org.vse.bookworm.dto.external.BookResponseDto;

public interface AppBookService {
    BookResponseDto getBook(BookRequestDto rqDto);
}
