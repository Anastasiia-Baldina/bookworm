package org.vse.bookworm.processor.cmd;

import org.vse.bookworm.dto.kafka.TextMessageDto;
import org.vse.bookworm.dto.kafka.TextResponseDto;

public interface CommandHandler {
    TextResponseDto handle(TextMessageDto msg);

    Command command();
}
