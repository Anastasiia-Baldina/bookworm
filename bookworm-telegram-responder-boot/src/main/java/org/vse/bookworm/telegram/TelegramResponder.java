package org.vse.bookworm.telegram;

import org.vse.bookworm.dto.kafka.Partitioned;

public interface TelegramResponder<T extends Partitioned> {
    void send(T response);
}
