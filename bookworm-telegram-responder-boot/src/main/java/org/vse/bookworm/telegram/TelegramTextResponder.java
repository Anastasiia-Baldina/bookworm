package org.vse.bookworm.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.vse.bookworm.dto.kafka.TextResponseDto;

public class TelegramTextResponder implements TelegramResponder<TextResponseDto> {
    private final TelegramBot bot;

    public TelegramTextResponder(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void send(TextResponseDto response) {
        SendMessage msg = new SendMessage(response.getChatId(), response.getText());
        bot.execute(msg);
    }
}
