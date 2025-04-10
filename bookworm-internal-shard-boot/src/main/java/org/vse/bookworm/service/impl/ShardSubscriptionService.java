package org.vse.bookworm.service.impl;

import org.vse.bookworm.dao.Chat;
import org.vse.bookworm.dao.Subscriber;
import org.vse.bookworm.dto.internal.ChatRequestDto;
import org.vse.bookworm.dto.internal.ChatResponseDto;
import org.vse.bookworm.dto.internal.JoinChatRequestDto;
import org.vse.bookworm.dto.internal.JoinChatResponseDto;
import org.vse.bookworm.dto.internal.SubscribeRequestDto;
import org.vse.bookworm.dto.internal.SubscribeResponseDto;
import org.vse.bookworm.dto.internal.UnsubscribeRequestDto;
import org.vse.bookworm.dto.internal.UnsubscribeResponseDto;
import org.vse.bookworm.repository.ChatRepository;
import org.vse.bookworm.repository.SubscriberRepository;
import org.vse.bookworm.service.SubscriptionService;

public class ShardSubscriptionService implements SubscriptionService {
    private final SubscriberRepository repository;
    private final ChatRepository chatRepository;

    public ShardSubscriptionService(SubscriberRepository repository,
                                    ChatRepository chatRepository) {
        this.repository = repository;
        this.chatRepository = chatRepository;
    }

    @Override
    public SubscribeResponseDto subscribe(SubscribeRequestDto requestDto) {
        var subscriber = Subscriber.builder()
                .setChatName(requestDto.getChatName())
                .setUserId(requestDto.getUserId())
                .setChatId(requestDto.getChatId())
                .build();
        repository.save(subscriber);

        return new SubscribeResponseDto()
                .setSuccess(true);
    }

    @Override
    public JoinChatResponseDto addChat(JoinChatRequestDto requestDto) {
        var chat = new Chat(requestDto.getChatId(), requestDto.getChatName());
        chatRepository.save(chat);
        return new JoinChatResponseDto()
                .setSuccess(true);
    }

    @Override
    public ChatResponseDto findChat(ChatRequestDto requestDto) {
        var chat = chatRepository.findByName(requestDto.getChatName());
        if (chat == null) {
            return new ChatResponseDto();
        }
        return new ChatResponseDto()
                .setSuccess(true)
                .setChatName(chat.getName())
                .setChatId(chat.getId());
    }

    @Override
    public UnsubscribeResponseDto unsubscribe(UnsubscribeRequestDto requestDto) {
        repository.delete(Subscriber.builder()
                .setChatId(requestDto.getChatId())
                .setUserId(requestDto.getUserId())
                .build());
        return new UnsubscribeResponseDto()
                .setSuccess(true);
    }
}
