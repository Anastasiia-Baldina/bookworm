package org.vse.bookworm.service;

import org.vse.bookworm.dto.internal.LoginRequestDto;
import org.vse.bookworm.dto.internal.LoginResponseDto;

public interface SessionService {

    LoginResponseDto login(LoginRequestDto request);
}
