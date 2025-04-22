package org.vse.bookworm.rest;

import org.springframework.web.client.RestTemplate;
import org.vse.bookworm.dto.internal.*;
import org.vse.bookworm.properties.FacadeProperties;

public class FacadeClient {
    private final RestTemplate rest;
    private final FacadeProperties cfg;

    public FacadeClient(FacadeProperties cfg, RestTemplate restTemplate) {
        this.rest = restTemplate;
        this.cfg = cfg;
    }

    public BookSaveResponseDto saveBook(BookSaveRequestDto rqDto) {
        var endpoint = cfg.getHost() + ":" + cfg.getPort();
        var url = "http://" + endpoint + "/book-worm/" + rqDto.getChatId() + "/book/save";
        return rest.postForObject(url, rqDto, BookSaveResponseDto.class);
    }

    public UserBookSaveResponseDto saveUserBook(UserBookSaveRequestDto rqDto) {
        var endpoint = cfg.getHost() + ":" + cfg.getPort();
        var url = "http://" + endpoint + "/book-worm/" + rqDto.getChatId() + "/user-book/save";
        return rest.postForObject(url, rqDto, UserBookSaveResponseDto.class);
    }

    public MessageListResponseDto listMessages(MessageListRequestDto rqDto) {
        var endpoint = cfg.getHost() + ":" + cfg.getPort();
        var url = "http://" + endpoint + "/book-worm/" + rqDto.getChatId() + "/message/list";
        return rest.postForObject(url, rqDto, MessageListResponseDto.class);
    }
}
