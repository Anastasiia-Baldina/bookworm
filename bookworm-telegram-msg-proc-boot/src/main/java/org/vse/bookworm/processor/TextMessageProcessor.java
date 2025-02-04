package org.vse.bookworm.processor;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vse.bookworm.dto.kafka.TextMessage;
import org.vse.bookworm.dto.kafka.TextResponse;
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

    public void process(List<TextMessage> msgList) {
        List<Future<RecordMetadata>> ftrList = new ArrayList<>();
        for (var msg : msgList) {
            try {
                log.info("Income message: {}", msg);
                TextResponse rsp = processMessage(msg);
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

    private TextResponse processMessage(TextMessage msg) {
        var text = msg.getText();
        if (text.startsWith("/")) {
            var txtCmd = extractCommandText(text);
            var cmd = Command.ofText(txtCmd);
            if (cmd == null) {
                return unknownCommand(txtCmd, msg.getChat().getId());
            }
            return cmdHandlers.get(cmd).handle(msg);
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

    private static TextResponse unknownCommand(String txtCmd, long chatId) {
        var commands = Command.values();
        List<String> lines = new ArrayList<>(commands.length + 2);
        lines.add("Неизвестная команда: " + txtCmd + '\n');
        lines.add("Доступные команды:\n");
        for (var cmd : commands) {
            lines.add(cmd.text() + ' ' + cmd.info());
        }
        return TextResponse.builder()
                .setChatId(chatId)
                .setText(String.join("\n", lines))
                .setAffinityKey(chatId)
                .build();
    }
}
