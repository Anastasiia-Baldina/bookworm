package org.vse.bookworm.processor.cmd;

import org.vse.bookworm.dto.kafka.TextMessageDto;
import org.vse.bookworm.dto.kafka.TextResponseDto;

public class CmdLoginHandler implements CommandHandler{
    @Override
    public TextResponseDto handle(TextMessageDto msg) {
        return TextResponseDto.builder()
                .setAffinityKey(msg.getAffinityKey())
                .setChatId(msg.getChat().getId())
                .setText("Код пользователя:123\nКод авторизации: 123456")
                .build();
    }

    @Override
    public Command command() {
        return Command.LOGIN;
    }
}
