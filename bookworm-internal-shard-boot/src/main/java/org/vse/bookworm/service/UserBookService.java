package org.vse.bookworm.service;

import org.vse.bookworm.dto.internal.UserBookSaveRequestDto;
import org.vse.bookworm.dto.internal.UserBookSaveResponseDto;

public interface UserBookService {
    UserBookSaveResponseDto save(UserBookSaveRequestDto requestDto);
}
