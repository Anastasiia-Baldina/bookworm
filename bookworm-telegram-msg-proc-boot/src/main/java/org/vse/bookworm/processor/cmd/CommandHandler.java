package org.vse.bookworm.processor.cmd;

import org.vse.bookworm.dto.kafka.TextMessage;
import org.vse.bookworm.dto.kafka.TextResponse;

public interface CommandHandler {
    TextResponse handle(TextMessage msg);

    Command command();
}
