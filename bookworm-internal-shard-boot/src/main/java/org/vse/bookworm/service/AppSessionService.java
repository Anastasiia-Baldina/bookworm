package org.vse.bookworm.service;

import org.vse.bookworm.dto.external.SignInRequestDto;
import org.vse.bookworm.dto.external.SignInResponseDto;

public interface AppSessionService {
    SignInResponseDto signIn(SignInRequestDto rqDto);
}
