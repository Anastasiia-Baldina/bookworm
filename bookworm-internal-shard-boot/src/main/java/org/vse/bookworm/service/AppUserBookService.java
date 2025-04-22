package org.vse.bookworm.service;

import org.vse.bookworm.dto.external.DeleteUserBookRequestDto;
import org.vse.bookworm.dto.external.DeleteUserBookResponseDto;
import org.vse.bookworm.dto.external.ListUserBookRequestDto;
import org.vse.bookworm.dto.external.ListUserBookResponseDto;

public interface AppUserBookService {
    ListUserBookResponseDto listBooks(ListUserBookRequestDto rqDto);

    DeleteUserBookResponseDto deleteBook(DeleteUserBookRequestDto rqDto);
}
