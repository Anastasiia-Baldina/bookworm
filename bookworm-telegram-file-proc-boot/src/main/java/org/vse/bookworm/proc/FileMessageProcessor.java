package org.vse.bookworm.proc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vse.bookworm.dto.kafka.FileMessage;
import org.vse.bookworm.dto.kafka.TextResponse;
import org.vse.bookworm.kafka.KafkaTextResponseProducer;
import org.vse.bookworm.parser.Fb2Parser;
import org.vse.bookworm.proc.properties.ProcessorProperties;
import org.vse.bookworm.utils.Asserts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FileMessageProcessor {
    private static final Logger log = LoggerFactory.getLogger(FileMessageProcessor.class);
    private static final String TOKEN_TEMPLATE = "https://api.telegram.org/file/bot#TOKEN#FILEPATH";
    private final String uriTemplate;
    private final int maxFileSize;
    private final KafkaTextResponseProducer kafka;

    public FileMessageProcessor(ProcessorProperties cfg,
                                KafkaTextResponseProducer kafkaTextResponseProducer) {
        this.uriTemplate = TOKEN_TEMPLATE.replace(
                "#TOKEN", Asserts.notEmpty(cfg.getTelegramTokenId(), "telegramTokenId"));
        this.maxFileSize = cfg.getMaxFileSize();
        this.kafka = kafkaTextResponseProducer;
    }

    public void process(List<FileMessage> msgList) {
        for (var msg : msgList) {
            try {
                processMessage(msg);
            } catch (Exception e) {
                log.error("Failed to process message: messageId={}", msg.getMessageId(), e);
            }
        }
    }

    private void processMessage(FileMessage msg) {
        if (msg.getFilename().endsWith(".fb2")) {
            sendResponse(msg, "Ошибка: файл должен иметь расширение .fb2");
        } else if (msg.getFileSize() > maxFileSize) {
            sendResponse(msg, "Ошибка: размер файла не должен превышать " + maxFileSize + "  байт.");
        } else {
            try {
                var url = URI.create(uriTemplate.replace("#FILEPATH", msg.getPath())).toURL();
                //var fileEntry = readTextFile(url.openStream());
                var fBook = new Fb2Parser().parse(url.openStream());
                sendResponse(msg, "Файл " + msg.getFilename() + "обработан");
            } catch (Exception e) {
                log.error("Failed to process file: messageId={}", msg.getMessageId(), e);
                sendResponse(msg, "Ошибка обработки файла. Повторите операцию позднее");
            }
        }
    }

    private static String readTextFile(InputStream is) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try(var reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        }
        return stringBuilder.toString();
    }

    private void sendResponse(FileMessage msg, String text) {
        log.info("Send response: {}, messageId={}", text, msg.getMessageId());

        var rsp = TextResponse.builder()
                .setText(text)
                .setChatId(msg.getChat().getId())
                .setAffinityKey(msg.getAffinityKey())
                .build();
        try {
            var meta = kafka.send(rsp).get();
            log.info("Response commited: messageId={},p={},ofs={}", msg.getMessageId(), meta.partition(), meta.offset());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Kafka commit failed: messageId={}", msg.getMessageId(), e);
        } catch (ExecutionException e) {
            log.error("Kafka commit failed: messageId={}", msg.getMessageId(), e);
        }
    }
}
