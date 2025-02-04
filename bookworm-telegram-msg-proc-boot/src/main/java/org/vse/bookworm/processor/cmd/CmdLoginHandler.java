package org.vse.bookworm.processor.cmd;

import org.vse.bookworm.dto.kafka.TextMessage;
import org.vse.bookworm.dto.kafka.TextResponse;

public class CmdLoginHandler implements CommandHandler{
    @Override
    public TextResponse handle(TextMessage msg) {
        return TextResponse.builder()
                .setAffinityKey(msg.getAffinityKey())
                .setChatId(msg.getChat().getId())
                .setText("Код авторизации: 123456")
                .build();
    }

    @Override
    public Command command() {
        return Command.LOGIN;
    }
}
