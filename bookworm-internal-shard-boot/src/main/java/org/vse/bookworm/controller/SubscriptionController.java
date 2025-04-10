package org.vse.bookworm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.vse.bookworm.dto.internal.*;
import org.vse.bookworm.service.SessionService;
import org.vse.bookworm.service.SubscriptionService;
import org.vse.bookworm.utils.Json;

@RestController
@RequestMapping("/book-worm")
public class SubscriptionController implements SubscriptionService {
    private static final Logger log = LoggerFactory.getLogger(SubscriptionController.class);
    private final SubscriptionService service;

    public SubscriptionController(SubscriptionService service) {
        this.service = service;
    }

    @Override
    @PostMapping(value = "/subscribe", produces = "application/json")
    @ResponseBody
    public SubscribeResponseDto subscribe(@RequestBody SubscribeRequestDto requestDto) {
        log.info("Request: /subscribe {}", Json.toJson(requestDto));
        return service.subscribe(requestDto);
    }

    @Override
    @PostMapping(value = "/add_chat", produces = "application/json")
    @ResponseBody
    public JoinChatResponseDto addChat(@RequestBody JoinChatRequestDto requestDto) {
        log.info("Request: /add_chat {}", Json.toJson(requestDto));
        return service.addChat(requestDto);
    }

    @Override
    @PostMapping(value = "/find_chat", produces = "application/json")
    @ResponseBody
    public ChatResponseDto findChat(@RequestBody ChatRequestDto requestDto) {
        log.info("Request: /find_chat {}", Json.toJson(requestDto));
        return service.findChat(requestDto);
    }
}
