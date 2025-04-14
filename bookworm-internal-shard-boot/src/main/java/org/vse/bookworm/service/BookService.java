package org.vse.bookworm.service;

import org.vse.bookworm.dto.internal.BookSaveRequestDto;
import org.vse.bookworm.dto.internal.BookSaveResponseDto;

public interface BookService {
    BookSaveResponseDto save(BookSaveRequestDto requestDto);
}
