package org.vse.bookworm.controller.external;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.vse.bookworm.dto.external.SignInRequestDto;
import org.vse.bookworm.dto.external.SignInResponseDto;
import org.vse.bookworm.service.AppSessionService;
import org.vse.bookworm.utils.Json;

@RestController
@RequestMapping("/book-worm")
public class AppSessionController {
    private static final Logger log = LoggerFactory.getLogger(AppSessionController.class);
    private final AppSessionService appSessionService;

    public AppSessionController(AppSessionService appSessionService) {
        this.appSessionService = appSessionService;
    }

    @PostMapping(value = "/sign-in", produces = "application/json")
    @ResponseBody
    public SignInResponseDto signIn(@RequestBody SignInRequestDto rqDto) {
        log.info("Request: /sign-in {}", Json.toJson(rqDto));
        return appSessionService.signIn(rqDto);
    }
}
