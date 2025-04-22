package org.vse.bookworm.processor.cmd;

import org.vse.bookworm.dto.kafka.FileMessageDto;
import org.vse.bookworm.dto.kafka.TextMessageDto;
import org.vse.bookworm.dto.kafka.TextResponseDto;
import org.vse.bookworm.kafka.KafkaFileRequestProducer;

public class CmdBuildHandler implements CommandHandler {
    private final KafkaFileRequestProducer kafkaFileRequestProducer;

    public CmdBuildHandler(KafkaFileRequestProducer kafkaFileRequestProducer) {
        this.kafkaFileRequestProducer = kafkaFileRequestProducer;
    }

    @Override
    public TextResponseDto handle(TextMessageDto msg, String[] args) {
        var fileMsg = FileMessageDto.builder()
                .setAffinityKey(msg.getAffinityKey())
                .setCategory(args[0])
                .setGenerated(true)
                .setChat(msg.getChat())
                .setUser(msg.getSender())
                .build();
        try {
            kafkaFileRequestProducer.send(fileMsg).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return TextResponseDto.builder()
                .setText("Формирую файл. Это займет несколько минут...")
                .setChatId(msg.getChat().getId())
                .setAffinityKey(msg.getChat().getId())
                .build();
    }

    @Override
    public Command command() {
        return Command.BUILD;
    }
}
