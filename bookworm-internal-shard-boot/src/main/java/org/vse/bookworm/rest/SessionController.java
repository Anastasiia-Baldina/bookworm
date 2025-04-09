package org.vse.bookworm.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.vse.bookworm.dto.internal.LoginRequestDto;
import org.vse.bookworm.dto.internal.LoginResponseDto;
import org.vse.bookworm.dto.internal.RegisterDeviceRequestDto;
import org.vse.bookworm.dto.internal.RegisterDeviceResponseDto;
import org.vse.bookworm.service.SessionService;

@RestController
@RequestMapping("/book-worm")
public class SessionController implements SessionService {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping(value = "/*/login", produces = "application/json")
    @ResponseBody
    @Override
    public LoginResponseDto login(@RequestBody LoginRequestDto request) {
        return sessionService.login(request);
    }

    @PostMapping(value = "/*/register_device", produces = "application/json")
    @ResponseBody
    @Override
    public RegisterDeviceResponseDto registerDevice(@RequestBody RegisterDeviceRequestDto request) {
        return sessionService.registerDevice(request);
    }
}
