package org.vse.bookworm.proc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vse.bookworm.dto.internal.BookSaveRequestDto;
import org.vse.bookworm.dto.internal.MessageDto;
import org.vse.bookworm.dto.internal.MessageListRequestDto;
import org.vse.bookworm.dto.internal.UserBookSaveRequestDto;
import org.vse.bookworm.dto.kafka.ChatDto;
import org.vse.bookworm.dto.kafka.FileMessageDto;
import org.vse.bookworm.dto.kafka.FileResponseDto;
import org.vse.bookworm.dto.kafka.TextResponseDto;
import org.vse.bookworm.kafka.KafkaFileResponseProducer;
import org.vse.bookworm.kafka.KafkaTextResponseProducer;
import org.vse.bookworm.parser.JaxbFb2Parser;
import org.vse.bookworm.properties.ProcessorProperties;
import org.vse.bookworm.rest.FacadeClient;
import org.vse.bookworm.utils.Asserts;
import org.vse.bookworm.utils.Gzip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FileMessageProcessor {
    private static final Logger log = LoggerFactory.getLogger(FileMessageProcessor.class);
    private static final String TOKEN_TEMPLATE = "https://api.telegram.org/file/bot#TOKEN/#FILEPATH";
    private final String uriTemplate;
    private final int maxFileSize;
    private final KafkaTextResponseProducer kafkaTextProducer;
    private final KafkaFileResponseProducer kafkaFileProducer;
    private final FacadeClient facadeClient;

    public FileMessageProcessor(ProcessorProperties cfg,
                                KafkaTextResponseProducer kafkaTextResponseProducer,
                                KafkaFileResponseProducer kafkaFileResponseProducer,
                                FacadeClient facadeClient) {
        this.uriTemplate = TOKEN_TEMPLATE.replace(
                "#TOKEN", Asserts.notEmpty(cfg.getTelegramTokenId(), "telegramTokenId"));
        this.maxFileSize = cfg.getMaxFileSize();
        this.kafkaTextProducer = kafkaTextResponseProducer;
        this.kafkaFileProducer = kafkaFileResponseProducer;
        this.facadeClient = facadeClient;
    }

    public void process(List<FileMessageDto> msgList) {
        for (var msg : msgList) {
            try {
                processMessage(msg);
            } catch (Exception e) {
                log.error("Failed to process message: messageId={}", msg.getMessageId(), e);
            }
        }
    }

    private void processMessage(FileMessageDto fileDto) {
        if (fileDto.isGenerated()) {
            generateFile(fileDto);
        } else {
            processIncomeFile(fileDto);
        }
    }

    private void generateFile(FileMessageDto fileDto) {
        var chat = fileDto.getChat();
        var chatId = chat.getId();
        var category = fileDto.getCategory();
        List<MessageDto> msgList = new ArrayList<>();
        String fileEntry;
        String filename;

        try {
            Long lastMsgId = -1L;
            do {
                var rq = new MessageListRequestDto()
                        .setLimit(100)
                        .setCategory(category)
                        .setStartMessageId(lastMsgId + 1)
                        .setChatId(chatId);
                var res = facadeClient.listMessages(rq).getMessages();
                if (res.isEmpty()) {
                    lastMsgId = null;
                } else {
                    for (var msg : res) {
                        if (msg.getMessageId() > lastMsgId) {
                            lastMsgId = msg.getMessageId();
                        }
                        msgList.add(msg);
                    }
                }
            } while (lastMsgId != null);
            if (msgList.isEmpty()) {
                sendResponse(fileDto, "Ошибка: сообщения с тегом '" + category + "' не найдены.");
                return;
            }
            fileEntry = generateFileEntry(chat, category, msgList);
            filename = category.replace("#", "")
                    .replace(" ", "_") + ".fb2";
            saveBook(fileDto, fileEntry, filename);
        } catch (Exception e) {
            log.error("Failed to process file: messageId={}", fileDto.getMessageId(), e);
            sendResponse(fileDto, "Ошибка обработки файла. Повторите операцию позднее.");
            return;
        }
        var fileRsp = new FileResponseDto()
                .setAffinityKey(chatId)
                .setChatId(chatId)
                .setFilename(filename)
                .setBase64Entry(Gzip.compressToBase64(fileEntry));
        try {
            var meta = kafkaFileProducer.send(fileRsp).get();
            log.info("Book sent: chatId={}, filename={}, p={}, ofs={}",
                    chatId, filename, meta.partition(), meta.offset());
        } catch (Exception e) {
            log.error("Send to kafka failed.", e);
        }
    }

    private String generateFileEntry(ChatDto chat, String tag, List<MessageDto> msgList) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<FictionBook xmlns=\"http://www.gribuser.ru/xml/fictionbook/2.0\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n");

        sb.append("<description>\n");
        sb.append("<title-info>\n");
        sb.append("<author>\n");
        sb.append("<first-name>").append(chat.getUserName()).append("</first-name>\n");
        sb.append("</author>\n");
        sb.append("<book-title>").append(chat.getTitle()).append(' ').append(tag).append("</book-title>\n");
        sb.append("</title-info>\n");
        sb.append("</description>\n");

        sb.append("<body>\n");
        sb.append("<section>\n");
        for (var msg : msgList) {
            sb.append("<p>").append(msg.getText()).append("</p>\n");
        }
        sb.append("</section>\n");
        sb.append("</body>\n");

        sb.append("</FictionBook>");
        return sb.toString();
    }

    private void processIncomeFile(FileMessageDto fileDto) {
        if (!fileDto.getFilename().endsWith(".fb2")) {
            sendResponse(fileDto, "Ошибка: файл должен иметь расширение .fb2");
        } else if (fileDto.getFileSize() > maxFileSize) {
            sendResponse(fileDto, "Ошибка: размер файла не должен превышать " + maxFileSize + "  байт.");
        } else {
            try {
                var url = URI.create(uriTemplate.replace("#FILEPATH", fileDto.getPath())).toURL();
                var fileEntry = readTextFile(url.openStream());
                var fBook = JaxbFb2Parser.getInstance().parse(fileEntry);
                saveBook(fileDto, fileEntry, fileDto.getFilename());
                sendResponse(fileDto, "Файл " + fileDto.getFilename() + " обработан.");
            } catch (Exception e) {
                log.error("Failed to process file: messageId={}", fileDto.getMessageId(), e);
                sendResponse(fileDto, "Ошибка обработки файла. Повторите операцию позднее.");
            }
        }
    }

    private void saveBook(FileMessageDto fileDto, String fileEntry, String filename) {
        var chat = fileDto.getChat();
        var user = fileDto.getUser();
        var chatId = chat.getId();
        var userId = user.getId();
        var bookId = chatId + "_" + filename;
        var bookV = Math.abs(fileEntry.hashCode());
        var chatName = (userId == chatId)
                ? user.getFirstName() + ' ' + user.getLastName()
                : chat.getTitle();
        var rsp = facadeClient.saveBook(new BookSaveRequestDto()
                .setBookId(bookId)
                .setBookVersion(bookV)
                .setFileBase64(Gzip.compressToBase64(fileEntry))
                .setChatId(chatId));
        if (rsp.isSuccess()) {
            if (userId == chatId) {
                facadeClient.saveUserBook(new UserBookSaveRequestDto()
                        .setBookId(bookId)
                        .setBookVersion(bookV)
                        .setUserId(userId)
                        .setPosition(0)
                        .setCurrentChapter(0)
                        .setUpdateTime(Instant.now().toEpochMilli())
                        .setProgress(0)
                        .setChatId(chatId)
                        .setChatName(chatName));
            } else {
                for (var memberId : rsp.getUserIds()) {
                    facadeClient.saveUserBook(new UserBookSaveRequestDto()
                            .setBookId(bookId)
                            .setBookVersion(bookV)
                            .setUserId(memberId)
                            .setPosition(0)
                            .setCurrentChapter(0)
                            .setUpdateTime(Instant.now().toEpochMilli())
                            .setProgress(0)
                            .setChatId(chatId)
                            .setChatName(chatName));
                }
            }
        }
    }

    private static String readTextFile(InputStream is) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (var reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        }
        return stringBuilder.toString();
    }

    private void sendResponse(FileMessageDto fileDto, String text) {
        log.info("Send response: {}, messageId={}", text, fileDto.getMessageId());

        var rsp = TextResponseDto.builder()
                .setText(text)
                .setChatId(fileDto.getChat().getId())
                .setAffinityKey(fileDto.getAffinityKey())
                .build();
        try {
            var meta = kafkaTextProducer.send(rsp).get();
            log.info("Response commited: messageId={},p={},ofs={}", fileDto.getMessageId(), meta.partition(), meta.offset());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Kafka commit failed: messageId={}", fileDto.getMessageId(), e);
        } catch (ExecutionException e) {
            log.error("Kafka commit failed: messageId={}", fileDto.getMessageId(), e);
        }
    }
}
