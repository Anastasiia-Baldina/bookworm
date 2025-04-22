package org.vse.bookworm.controller.external;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.vse.bookworm.dto.external.BookRequestDto;
import org.vse.bookworm.dto.external.BookResponseDto;
import org.vse.bookworm.dto.external.ListUserBookRequestDto;
import org.vse.bookworm.dto.external.ListUserBookResponseDto;
import org.vse.bookworm.service.AppBookService;
import org.vse.bookworm.utils.Json;

@RestController
@RequestMapping("/book-worm")
public class AppBookController {
    private static final Logger log = LoggerFactory.getLogger(AppBookController.class);
    private final AppBookService appBookService;

    public AppBookController(AppBookService appBookService) {
        this.appBookService = appBookService;
    }

    @PostMapping(value = "/get-book", produces = "application/json")
    @ResponseBody
    public BookResponseDto signIn(@RequestBody BookRequestDto rqDto) {
        log.info("Request: /get-book {}", Json.toJson(rqDto));
        return appBookService.getBook(rqDto);
    }
}
