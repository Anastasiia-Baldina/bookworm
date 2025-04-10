package org.vse.bookworm.service.impl;

import org.vse.bookworm.dto.internal.LoginRequestDto;
import org.vse.bookworm.dto.internal.LoginResponseDto;
import org.vse.bookworm.dto.internal.RegisterDeviceRequestDto;
import org.vse.bookworm.dto.internal.RegisterDeviceResponseDto;
import org.vse.bookworm.repository.SessionRepository;
import org.vse.bookworm.dao.Session;
import org.vse.bookworm.service.SessionService;
import org.vse.bookworm.properties.SessionProperties;
import org.vse.bookworm.utils.Asserts;
import org.vse.bookworm.utils.IdGenerator;
import org.vse.bookworm.utils.Rnd;

import java.time.Instant;

public class ShardSessionService implements SessionService {
    private final SessionProperties sessionProperties;
    private final SessionRepository sessionRepository;
    private final IdGenerator idGenerator;

    public ShardSessionService(SessionProperties sessionProperties,
                               SessionRepository sessionRepository,
                               IdGenerator idGenerator) {
        this.sessionProperties = sessionProperties;
        this.sessionRepository = sessionRepository;
        this.idGenerator = idGenerator;
    }

    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        var userId = request.getUserId();
        var username = request.getUserName();
        int code = Math.abs(Rnd.nextInt() % sessionProperties.getMaxAcceptCode());
        var session = Session.builder()
                .setId(idGenerator.get())
                .setUsername(username)
                .setUserId(userId)
                .setCreateTime(Instant.now())
                .setAcceptCode(code)
                .build();
        sessionRepository.create(session);

        return new LoginResponseDto()
                .setUserId(userId)
                .setAcceptCode(code);
    }

    @Override
    public RegisterDeviceResponseDto registerDevice(RegisterDeviceRequestDto request) {
        Asserts.notEmpty(request.getDeviceId(), "deviceId");
        var optSession = sessionRepository.find(request.getUserId()).stream()
                .filter(x -> x.getUpdateTime() == null)
                .findFirst();
        if(optSession.isEmpty()) {
            return new RegisterDeviceResponseDto()
                    .setErrorMessage("Сессия пользователя не найдена. Введите команду /login в Telegram-боте");
        }
        var session = optSession.get().toBuilder()
                .setDeviceId(request.getDeviceId())
                .setDeviceName(request.getDeviceName())
                .setUpdateTime(Instant.now())
                .setAcceptCode(request.getAcceptCode())
                .build();
        if(sessionRepository.attachDevice(session)) {
            return new RegisterDeviceResponseDto()
                    .setSuccess(true)
                    .setSessionId(session.getId());
        } else {
            return new RegisterDeviceResponseDto()
                    .setErrorMessage("Неверный проверочный код");
        }
    }
}
