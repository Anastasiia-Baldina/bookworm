package org.vse.bookworm.processor.cmd;

import org.vse.bookworm.dto.kafka.TextMessage;
import org.vse.bookworm.dto.kafka.TextResponse;

import java.util.stream.Stream;

public class CmdStartHandler implements CommandHandler {
    private static final String REPLY;

    static {
        var txtLines = Stream.of(Command.values())
                .map(cmd -> cmd.text() + ' ' + cmd.info())
                .toList();
        REPLY = "Доступные команды: \n\n"+
                String.join("\n", txtLines);
    }

    @Override
    public TextResponse handle(TextMessage msg) {
        return TextResponse.builder()
                .setChatId(msg.getChat().getId())
                .setAffinityKey(msg.getAffinityKey())
                .setText(REPLY)
                .build();
    }

    @Override
    public Command command() {
        return Command.START;
    }


}
