package org.vse.bookworm.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.vse.bookworm.dto.internal.LoginRequestDto;
import org.vse.bookworm.dto.internal.LoginResponseDto;
import org.vse.bookworm.service.SessionService;

@RestController
@RequestMapping("/book-worm")
public class SessionController {
    private SessionService sessionService;

    @PostMapping(value = "/{id}/login", produces = "application/json")
    @ResponseBody
    public LoginResponseDto login(@RequestParam("id") long id, @RequestBody LoginRequestDto request) {
        return sessionService.login(request);
    }
}
