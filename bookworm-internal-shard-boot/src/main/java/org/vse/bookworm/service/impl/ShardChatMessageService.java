package org.vse.bookworm.service.impl;

import org.vse.bookworm.dao.ChatMessage;
import org.vse.bookworm.dao.Message;
import org.vse.bookworm.dto.internal.MessageSaveRequestDto;
import org.vse.bookworm.dto.internal.MessageSaveResponseDto;
import org.vse.bookworm.dto.internal.DeleteMessageRequestDto;
import org.vse.bookworm.dto.internal.DeleteMessageResponseDto;
import org.vse.bookworm.repository.ChatMessageRepository;
import org.vse.bookworm.service.ChatMessageService;

public class ShardChatMessageService implements ChatMessageService {
    private final ChatMessageRepository repository;

    public ShardChatMessageService(ChatMessageRepository repository) {
        this.repository = repository;
    }

    @Override
    public MessageSaveResponseDto saveMessage(MessageSaveRequestDto requestDto) {
        repository.save(ChatMessage.builder()
                .setChatId(requestDto.getChatId())
                .setMessageId(requestDto.getMessageId())
                .setCategory(requestDto.getCategory())
                .setUsername(requestDto.getUsername())
                .setMessage(Message.builder()
                        .setText(requestDto.getMessage())
                        .build())
                .build());
        return new MessageSaveResponseDto()
                .setSuccess(true);
    }

    @Override
    public DeleteMessageResponseDto deleteMessage(DeleteMessageRequestDto requestDto) {
        repository.delete(requestDto.getChatId(), requestDto.getMessageId());
        return new DeleteMessageResponseDto()
                .setSuccess(true);
    }
}
