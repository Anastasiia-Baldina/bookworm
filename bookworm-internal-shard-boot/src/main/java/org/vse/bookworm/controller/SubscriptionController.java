package org.vse.bookworm.controller;

import org.springframework.web.bind.annotation.*;
import org.vse.bookworm.dto.internal.*;
import org.vse.bookworm.service.SessionService;
import org.vse.bookworm.service.SubscriptionService;

@RestController
@RequestMapping("/book-worm")
public class SubscriptionController implements SubscriptionService {
    private final SubscriptionService service;

    public SubscriptionController(SubscriptionService service) {
        this.service = service;
    }

    @PostMapping(value = "/subscribe", produces = "application/json")
    @ResponseBody
    @Override
    public SubscribeResponseDto subscribe(SubscribeRequestDto requestDto) {
        return service.subscribe(requestDto);
    }
}
