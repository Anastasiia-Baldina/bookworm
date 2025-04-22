package org.vse.bookworm.service.impl;

import org.vse.bookworm.dao.ChatMessage;
import org.vse.bookworm.dao.Message;
import org.vse.bookworm.dto.internal.*;
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

    @Override
    public MessageListResponseDto listMessage(MessageListRequestDto requestDto) {
        var msgList = repository.list(requestDto.getChatId(), requestDto.getCategory(), requestDto.getStartMessageId(), requestDto.getLimit())
                .stream()
                .map(x -> new MessageDto()
                        .setMessageId(x.getMessageId())
                        .setText(x.getMessage().getText()))
                .toList();
        return new MessageListResponseDto()
                .setMessages(msgList)
                .setSuccess(true);
    }
}
