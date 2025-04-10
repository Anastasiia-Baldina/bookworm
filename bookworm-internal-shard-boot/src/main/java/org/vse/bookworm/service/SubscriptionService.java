package org.vse.bookworm.service;

import org.vse.bookworm.dto.internal.SubscribeRequestDto;
import org.vse.bookworm.dto.internal.SubscribeResponseDto;

public interface SubscriptionService {
    SubscribeResponseDto subscribe(SubscribeRequestDto requestDto);
}
