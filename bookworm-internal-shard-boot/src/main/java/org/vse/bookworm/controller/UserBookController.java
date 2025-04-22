package org.vse.bookworm.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.vse.bookworm.dto.internal.UserBookSaveRequestDto;
import org.vse.bookworm.dto.internal.UserBookSaveResponseDto;
import org.vse.bookworm.service.UserBookService;

@RestController
@RequestMapping("/book-worm/user-book")
public class UserBookController implements UserBookService {
    private final UserBookService userBookService;

    public UserBookController(UserBookService userBookService) {
        this.userBookService = userBookService;
    }

    @Override
    @PostMapping(value = "/save", produces = "application/json")
    @ResponseBody
    public UserBookSaveResponseDto save(@RequestBody UserBookSaveRequestDto requestDto) {
        return userBookService.save(requestDto);
    }
}
