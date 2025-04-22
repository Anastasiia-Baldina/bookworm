package org.vse.bookworm.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vse.bookworm.dto.kafka.FileResponseDto;
import org.vse.bookworm.utils.Gzip;

public class TelegramFileResponder implements TelegramResponder<FileResponseDto> {
    private static final Logger log = LoggerFactory.getLogger(TelegramFileResponder.class);
    private final TelegramBot bot;

    public TelegramFileResponder(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void send(FileResponseDto rsp) {
        var filename = rsp.getFilename();
        var chatId = rsp.getChatId();
        try {
            var fileEntry = Gzip.decompressFromBase64(rsp.getBase64Entry());
            SendDocument docRq = new SendDocument(chatId, fileEntry);
            docRq.fileName(filename);
            var tgRsp = bot.execute(docRq);
            log.info("File sent: filename={}, chatId={}", filename, chatId);
        } catch (Exception e) {
            log.error("Failed sent file: filename={}, chatId={}", filename, chatId);
        }
    }
}
