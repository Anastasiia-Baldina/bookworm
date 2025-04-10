package org.vse.bookworm.processor.cmd;

import org.vse.bookworm.dto.kafka.TextMessageDto;
import org.vse.bookworm.dto.kafka.TextResponseDto;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CmdStartHandler implements CommandHandler {
    @Override
    public TextResponseDto handle(TextMessageDto msg, String[] args) {
        var cmdStream = Stream.of(Command.values());
        if (isGroupMessage(msg)) {
            cmdStream = cmdStream.filter(Command::inGroup);
        }
        var text = "Доступные команды:\n" +
                cmdStream.map(x -> x.text() + " " + x.info())
                        .collect(Collectors.joining("\n"));
        return TextResponseDto.builder()
                .setChatId(msg.getChat().getId())
                .setAffinityKey(msg.getAffinityKey())
                .setText(text)
                .build();
    }

    @Override
    public Command command() {
        return Command.START;
    }

    private boolean isGroupMessage(TextMessageDto msgDto) {
        return msgDto.getChat() != null
                && msgDto.getSender() != null
                && msgDto.getSender().getId() != msgDto.getChat().getId();
    }
}
