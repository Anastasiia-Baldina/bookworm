package org.vse.bookworm.rest;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.vse.bookworm.dto.internal.LoginRequest;

@RestController
public class InternalFacadeController {
    private final ShardDownStream downStream;

    public InternalFacadeController(ShardDownStream downStream) {
        this.downStream = downStream;
    }

    public void login(@RequestBody LoginRequest loginRequest) {

    }
}
