package org.vse.bookworm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.vse.bookworm.dto.internal.*;
import org.vse.bookworm.service.ChatMessageService;
import org.vse.bookworm.utils.Json;

@RestController
@RequestMapping("/book-worm/message")
public class ChatMessageController implements ChatMessageService {
    private static final Logger log = LoggerFactory.getLogger(ChatMessageController.class);
    private final ChatMessageService messageService;

    public ChatMessageController(ChatMessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    @PostMapping(value = "/save", produces = "application/json")
    @ResponseBody
    public MessageSaveResponseDto saveMessage(@RequestBody MessageSaveRequestDto requestDto) {
        log.info("Request: /message/save {}", Json.toJson(requestDto));
        return messageService.saveMessage(requestDto);
    }

    @Override
    @PostMapping(value = "/delete", produces = "application/json")
    @ResponseBody
    public DeleteMessageResponseDto deleteMessage(@RequestBody DeleteMessageRequestDto requestDto) {
        log.info("Request: /message/delete {}", Json.toJson(requestDto));
        return messageService.deleteMessage(requestDto);
    }

    @Override
    @PostMapping(value = "/list", produces = "application/json")
    @ResponseBody
    public MessageListResponseDto listMessage(@RequestBody MessageListRequestDto requestDto) {
        log.info("Request: /message/list {}", Json.toJson(requestDto));
        return messageService.listMessage(requestDto);
    }
}
