package org.vse.bookworm.service.impl;

import org.vse.bookworm.dto.external.SignInRequestDto;
import org.vse.bookworm.dto.external.SignInResponseDto;
import org.vse.bookworm.repository.SessionRepository;
import org.vse.bookworm.service.AppSessionService;
import org.vse.bookworm.utils.Asserts;

import java.time.Instant;

public class ShardAppSessionService implements AppSessionService {
    private final SessionRepository sessionRepository;

    public ShardAppSessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public SignInResponseDto signIn(SignInRequestDto rqDto) {
        Asserts.notEmpty(rqDto.getDeviceId(), "deviceId");
        var optSession = sessionRepository.find(rqDto.getUserId()).stream()
                .filter(x -> x.getUpdateTime() == null)
                .findFirst();
        if(optSession.isEmpty()) {
            return new SignInResponseDto()
                    .setMessage("Сессия пользователя не найдена.");
        }
        var session = optSession.get().toBuilder()
                .setDeviceId(rqDto.getDeviceId())
                .setDeviceName(rqDto.getDeviceName())
                .setUpdateTime(Instant.now())
                .setAcceptCode(rqDto.getAcceptCode())
                .build();
        if(sessionRepository.attachDevice(session)) {
            return new SignInResponseDto()
                    .setSuccess(true)
                    .setSessionId(session.getId());
        } else {
            return new SignInResponseDto()
                    .setMessage("Неверный проверочный код");
        }
    }
}
