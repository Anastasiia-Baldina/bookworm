package org.vse.bookworm.rest;

import org.springframework.web.bind.annotation.*;
import org.vse.bookworm.dto.internal.LoginRequestDto;
import org.vse.bookworm.dto.internal.LoginResponseDto;
import org.vse.bookworm.service.SessionService;

@RestController
@RequestMapping("/book-worm")
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping(value = "/{id}/login", produces = "application/json")
    @ResponseBody
    public LoginResponseDto login(@PathVariable("id") long id, @RequestBody LoginRequestDto request) {
        return sessionService.login(request);
    }
}
