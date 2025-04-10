package org.vse.bookworm.processor.cmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vse.bookworm.dto.internal.LoginRequestDto;
import org.vse.bookworm.dto.kafka.TextMessageDto;
import org.vse.bookworm.dto.kafka.TextResponseDto;
import org.vse.bookworm.rest.FacadeClient;

public class CmdLoginHandler implements CommandHandler {
    private static final Logger log = LoggerFactory.getLogger(CmdLoginHandler.class);
    private final FacadeClient facadeClient;

    public CmdLoginHandler(FacadeClient facadeClient) {
        this.facadeClient = facadeClient;
    }

    @Override
    public TextResponseDto handle(TextMessageDto msg, String[] args) {
        if (isGroupMessage(msg)) {
            return responseBuilder(msg)
                    .setText("Команда доступна только через Telegram-bot")
                    .build();
        }

        try {
            var user = msg.getSender();
            var rq = new LoginRequestDto()
                    .setUserId(user.getId())
                    .setFirstName(user.getFirstName())
                    .setLastName(user.getLastName())
                    .setUserName(user.getUserName());
            var response = facadeClient.login(rq);
            if (response != null) {
                return responseBuilder(msg)
                        .setText("Код пользователя: " +
                                response.getUserId() +
                                "\nКод авторизации: " +
                                response.getAcceptCode())
                        .build();
            }
        } catch (Exception e) {
            log.error("Error processing login command", e);
        }
        return responseBuilder(msg)
                .setText("Ошибка выполнения. Повторите операцию позднее.")
                .build();
    }

    private TextResponseDto.Builder responseBuilder(TextMessageDto msg) {
        return TextResponseDto.builder()
                .setAffinityKey(msg.getAffinityKey())
                .setChatId(msg.getChat().getId());
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
