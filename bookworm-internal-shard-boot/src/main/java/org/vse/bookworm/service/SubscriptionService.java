package org.vse.bookworm.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.vse.bookworm.dto.internal.ChatRequestDto;
import org.vse.bookworm.dto.internal.ChatResponseDto;
import org.vse.bookworm.dto.internal.ChatJoinRequestDto;
import org.vse.bookworm.dto.internal.ChatJoinResponseDto;
import org.vse.bookworm.dto.internal.SubscribeRequestDto;
import org.vse.bookworm.dto.internal.SubscribeResponseDto;
import org.vse.bookworm.dto.internal.UnsubscribeRequestDto;
import org.vse.bookworm.dto.internal.UnsubscribeResponseDto;

public interface SubscriptionService {
    SubscribeResponseDto subscribe(SubscribeRequestDto requestDto);

    ChatJoinResponseDto addChat(ChatJoinRequestDto requestDto);

    ChatResponseDto findChat(ChatRequestDto requestDto);

    UnsubscribeResponseDto unsubscribe(@RequestBody UnsubscribeRequestDto requestDto);
}
