package org.vse.bookworm.processor.cmd;

import org.vse.bookworm.dto.kafka.TextMessageDto;
import org.vse.bookworm.dto.kafka.TextResponseDto;

import java.util.List;

public interface CommandHandler {
    TextResponseDto handle(TextMessageDto msg, String[] args);

    Command command();
}
