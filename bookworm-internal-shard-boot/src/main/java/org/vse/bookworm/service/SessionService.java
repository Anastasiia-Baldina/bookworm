package org.vse.bookworm.service;

import org.vse.bookworm.dto.internal.LoginRequestDto;
import org.vse.bookworm.dto.internal.LoginResponseDto;
import org.vse.bookworm.dto.internal.RegisterDeviceRequestDto;
import org.vse.bookworm.dto.internal.RegisterDeviceResponseDto;

public interface SessionService {

    LoginResponseDto login(LoginRequestDto request);

    RegisterDeviceResponseDto registerDevice(RegisterDeviceRequestDto request);
}
