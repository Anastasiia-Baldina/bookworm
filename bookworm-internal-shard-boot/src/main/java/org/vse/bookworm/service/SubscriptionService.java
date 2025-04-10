package org.vse.bookworm.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.vse.bookworm.dto.internal.*;

public interface SubscriptionService {
    SubscribeResponseDto subscribe(SubscribeRequestDto requestDto);

    JoinChatResponseDto addChat(JoinChatRequestDto requestDto);

    ChatResponseDto findChat(ChatRequestDto requestDto);
}
