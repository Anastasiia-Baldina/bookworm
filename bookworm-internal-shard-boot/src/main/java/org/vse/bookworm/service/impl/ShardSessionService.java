package org.vse.bookworm.service.impl;

import org.vse.bookworm.dto.internal.LoginRequestDto;
import org.vse.bookworm.dto.internal.LoginResponseDto;
import org.vse.bookworm.repository.SessionRepository;
import org.vse.bookworm.repository.UserRepository;
import org.vse.bookworm.repository.model.Session;
import org.vse.bookworm.repository.model.User;
import org.vse.bookworm.service.SessionService;
import org.vse.bookworm.service.properties.SessionProperties;
import org.vse.bookworm.utils.IdGenerator;
import org.vse.bookworm.utils.Rnd;

import java.time.Duration;
import java.time.Instant;

public class ShardSessionService implements SessionService {
    private final SessionProperties sessionProperties;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final IdGenerator idGenerator;

    public ShardSessionService(SessionProperties sessionProperties,
                               UserRepository userRepository,
                               SessionRepository sessionRepository,
                               IdGenerator idGenerator) {
        this.sessionProperties = sessionProperties;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.idGenerator = idGenerator;
    }

    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        var userId = request.getUserId();
        var user = userRepository.get(userId);
        var username = request.getUserName();

        if(user == null) {
            user = User.builder()
                    .setId(userId)
                    .setFirstName(request.getFirstName())
                    .setLastName(request.getLastName())
                    .setUserName(username)
                    .build();
            userRepository.create(user);
        }
        int code = Math.abs(Rnd.nextInt() % sessionProperties.getMaxLoginCodeSize());
        var duration = Duration.ofSeconds(sessionProperties.getInactivationIntervalSec());
        var session = Session.builder()
                .setId(idGenerator.get())
                .setUserId(userId)
                .setLoginCode(code)
                .setExpiredAt(Instant.now().plus(duration))
                .setCreateTime(Instant.now())
                .build();
        sessionRepository.create(session);

        return new LoginResponseDto()
                .setUserId(userId)
                .setLoginCode(code)
                .setUserName(username);
    }
}
