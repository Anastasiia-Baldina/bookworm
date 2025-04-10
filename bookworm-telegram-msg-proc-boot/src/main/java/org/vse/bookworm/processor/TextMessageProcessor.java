package org.vse.bookworm.processor;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vse.bookworm.dto.kafka.TextMessageDto;
import org.vse.bookworm.dto.kafka.TextResponseDto;
import org.vse.bookworm.kafka.KafkaTextResponseProducer;
import org.vse.bookworm.processor.cmd.Command;
import org.vse.bookworm.processor.cmd.CommandHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class TextMessageProcessor {
    private static final Logger log = LoggerFactory.getLogger(TextMessageProcessor.class);
    private final Map<Command, CommandHandler> cmdHandlers;
    private final KafkaTextResponseProducer kafka;

    public TextMessageProcessor(List<CommandHandler> cmdHandlers,
                                KafkaTextResponseProducer kafkaTextResponseProducer) {
        this.cmdHandlers = cmdHandlers.stream()
                .collect(Collectors.toMap(CommandHandler::command, x -> x));
        this.kafka = kafkaTextResponseProducer;
    }

    public void process(List<TextMessageDto> msgList) {
        List<Future<RecordMetadata>> ftrList = new ArrayList<>();
        for (var msg : msgList) {
            try {
                log.info("Income message: {}", msg);
                TextResponseDto rsp = processMessage(msg);
                if (rsp != null) {
                    log.info("Response: {}", rsp);
                    ftrList.add(kafka.send(rsp));
                }
            } catch (Exception e) {
                log.error("Failed to process message. messageId={}", msg.getMessageId(), e);
            }
        }
        ftrList.forEach(f -> {
            try {
                f.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Kafka commit error.", e);
            } catch (ExecutionException e) {
                log.error("Kafka commit error.", e);
            }
        });
    }

    private TextResponseDto processMessage(TextMessageDto msg) {
        var textParts = msg.getText().split(" ");
        if (textParts.length > 0 && textParts[0].startsWith("/")) {
            var txtCmd = textParts[0];
            var cmd = Command.ofText(txtCmd);
            long chatId = msg.getChat().getId();
            if (cmd == null) {
                return unknownCommand(txtCmd, chatId);
            }
            if (cmd.argCount() >= textParts.length) {
                return mismatchArguments(cmd, chatId);
            }
            String[] args = new String[cmd.argCount()];
            for (int i = 1; i < cmd.argCount(); i++) {
                args[i - 1] = textParts[i];
            }
            return cmdHandlers.get(cmd).handle(msg, args);
        }
        return null;
    }

    private String extractCommandText(String text) {
        int spacePos = text.indexOf(' ');
        if (spacePos == -1) {
            return text;
        } else {
            return text.substring(0, spacePos - 1);
        }
    }

    private static TextResponseDto unknownCommand(String txtCmd, long chatId) {
        String text = "Неизвестная команда: " + txtCmd + '\n' +
                "/start - для вывода списка команд";
        return TextResponseDto.builder()
                .setChatId(chatId)
                .setText(String.join("\n", text))
                .setAffinityKey(chatId)
                .build();
    }

    private static TextResponseDto mismatchArguments(Command cmd, long chatId) {
        String text = "Некорректный формат команды.\n" +
                "Пример использования:\n" +
                cmd.usage();
        return TextResponseDto.builder()
                .setChatId(chatId)
                .setText(String.join("\n", text))
                .setAffinityKey(chatId)
                .build();
    }
}
