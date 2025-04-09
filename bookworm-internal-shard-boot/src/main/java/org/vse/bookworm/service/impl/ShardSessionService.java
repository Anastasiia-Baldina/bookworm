package org.vse.bookworm.service.impl;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vse.bookworm.dto.internal.LoginRequestDto;
import org.vse.bookworm.dto.internal.LoginResponseDto;
import org.vse.bookworm.repository.SessionRepository;
import org.vse.bookworm.dao.Session;
import org.vse.bookworm.service.SessionService;
import org.vse.bookworm.properties.SessionProperties;
import org.vse.bookworm.utils.IdGenerator;

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

        //int code = Math.abs(Rnd.nextInt() % sessionProperties.getMaxLoginCodeSize());
        var session = Session.builder()
                .setId(idGenerator.get())
                .setUsername(username)
                .setUserId(userId)
                .setUpdateTime(Instant.now())
                .setCreateTime(Instant.now())
                .build();
        sessionRepository.create(session);

        return new LoginResponseDto()
                .setUserId(userId);
    }
}
