package org.vse.bookworm.controller.external;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.vse.bookworm.dto.external.DeleteUserBookRequestDto;
import org.vse.bookworm.dto.external.DeleteUserBookResponseDto;
import org.vse.bookworm.dto.external.ListUserBookRequestDto;
import org.vse.bookworm.dto.external.ListUserBookResponseDto;
import org.vse.bookworm.service.AppUserBookService;
import org.vse.bookworm.utils.Json;

@RestController
@RequestMapping("/book-worm")
public class AppUserBookController {
    private static final Logger log = LoggerFactory.getLogger(AppSessionController.class);
    private final AppUserBookService userBookService;

    public AppUserBookController(AppUserBookService userBookService) {
        this.userBookService = userBookService;
    }

    @PostMapping(value = "/list-books", produces = "application/json")
    @ResponseBody
    public ListUserBookResponseDto signIn(@RequestBody ListUserBookRequestDto rqDto) {
        log.info("Request: /list-books {}", Json.toJson(rqDto));
        return userBookService.listBooks(rqDto);
    }

    @PostMapping(value = "/delete-user-book", produces = "application/json")
    @ResponseBody
    public DeleteUserBookResponseDto deleteUserBook(@RequestBody DeleteUserBookRequestDto rqDto) {
        log.info("Request: /delete-user-book {}", Json.toJson(rqDto));
        return userBookService.deleteBook(rqDto);
    }
}
