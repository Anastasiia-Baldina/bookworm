package org.vse.bookworm.service.impl;

import org.vse.bookworm.dto.internal.SubscribeRequestDto;
import org.vse.bookworm.dto.internal.SubscribeResponseDto;
import org.vse.bookworm.repository.SubscriberRepository;
import org.vse.bookworm.service.SubscriptionService;

public class ShardSubscriptionService implements SubscriptionService {
    private final SubscriberRepository repository;

    public ShardSubscriptionService(SubscriberRepository repository) {
        this.repository = repository;
    }

    @Override
    public SubscribeResponseDto subscribe(SubscribeRequestDto requestDto) {
        return null;
    }
}
