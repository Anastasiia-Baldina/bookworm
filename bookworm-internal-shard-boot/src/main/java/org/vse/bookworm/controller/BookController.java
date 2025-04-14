package org.vse.bookworm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.vse.bookworm.dto.internal.*;
import org.vse.bookworm.service.BookService;
import org.vse.bookworm.service.ChatMessageService;
import org.vse.bookworm.utils.Json;

@RestController
@RequestMapping("/book-worm/book")
public class BookController implements BookService {
    private static final Logger log = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    @PostMapping(value = "/save", produces = "application/json")
    @ResponseBody
    public BookSaveResponseDto save(@RequestBody BookSaveRequestDto requestDto) {
        return bookService.save(requestDto);
    }
}
