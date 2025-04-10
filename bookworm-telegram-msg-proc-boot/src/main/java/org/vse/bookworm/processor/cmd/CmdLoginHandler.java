package org.vse.bookworm.processor.cmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.vse.bookworm.dto.internal.LoginRequestDto;
import org.vse.bookworm.dto.internal.LoginResponseDto;
import org.vse.bookworm.dto.kafka.TextMessageDto;
import org.vse.bookworm.dto.kafka.TextResponseDto;
import org.vse.bookworm.kafka.FacadeProperties;

public class CmdLoginHandler implements CommandHandler {
    private static final Logger log = LoggerFactory.getLogger(CmdLoginHandler.class);
    private final FacadeProperties cfg;
    private final RestTemplate restTemplate;

    public CmdLoginHandler(FacadeProperties cfg, RestTemplate restTemplate) {
        this.cfg = cfg;
        this.restTemplate = restTemplate;
    }

    @Override
    public TextResponseDto handle(TextMessageDto msg, String[] args) {
        if (isGroupMessage(msg)) {
            return TextResponseDto.builder()
                    .setAffinityKey(msg.getAffinityKey())
                    .setChatId(msg.getChat().getId())
                    .setText("Команда доступна только через Telegram-bot")
                    .build();
        }

        var endpoint = cfg.getHost() + ":" + cfg.getPort();
        var url = "http://" + endpoint + "/book-worm/" + msg.getSender().getId() + "/login";
        try {
            var user = msg.getSender();
            var rq = new LoginRequestDto()
                    .setUserId(user.getId())
                    .setFirstName(user.getFirstName())
                    .setLastName(user.getLastName())
                    .setUserName(user.getUserName());
            var response = restTemplate.postForObject(url, rq, LoginResponseDto.class);
            if (response != null) {
                return TextResponseDto.builder()
                        .setAffinityKey(msg.getAffinityKey())
                        .setChatId(msg.getChat().getId())
                        .setText("Код пользователя: " +
                                response.getUserId() +
                                "\nКод авторизации: " +
                                response.getAcceptCode())
                        .build();
            }
        } catch (Exception e) {
            log.error("Error processing login command", e);
        }
        return TextResponseDto.builder()
                .setAffinityKey(msg.getAffinityKey())
                .setChatId(msg.getChat().getId())
                .setText("Ошибка выполнения. Попробуйте позже.")
                .build();
    }

    @Override
    public Command command() {
        return Command.LOGIN;
    }

    private boolean isGroupMessage(TextMessageDto msgDto) {
        return msgDto.getChat() != null
                && msgDto.getSender() != null
                && msgDto.getSender().getId() != msgDto.getChat().getId();
    }
}
