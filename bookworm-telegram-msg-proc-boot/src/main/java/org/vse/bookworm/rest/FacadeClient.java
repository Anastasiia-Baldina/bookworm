package org.vse.bookworm.rest;

import org.springframework.web.client.RestTemplate;
import org.vse.bookworm.dto.internal.*;
import org.vse.bookworm.kafka.FacadeProperties;

public class FacadeClient {
    private final RestTemplate rest;
    private final FacadeProperties cfg;

    public FacadeClient(FacadeProperties cfg, RestTemplate restTemplate) {
        this.rest = restTemplate;
        this.cfg = cfg;
    }

    public LoginResponseDto login(LoginRequestDto rqDto) {
        var endpoint = cfg.getHost() + ":" + cfg.getPort();
        var url = "http://" + endpoint + "/book-worm/" + rqDto.getUserId() + "/login";
        return rest.postForObject(url, rqDto, LoginResponseDto.class);
    }

    public JoinChatResponseDto joinChat(JoinChatRequestDto rqDto) {
        var endpoint = cfg.getHost() + ":" + cfg.getPort();
        var url = "http://" + endpoint + "/book-worm/" + rqDto.getChatName().hashCode() + "/add_chat";
        return rest.postForObject(url, rqDto, JoinChatResponseDto.class);
    }

    public ChatResponseDto findChat(ChatRequestDto rqDto) {
        var endpoint = cfg.getHost() + ":" + cfg.getPort();
        var url = "http://" + endpoint + "/book-worm/" + rqDto.getChatName().hashCode() + "/find_chat";
        return rest.postForObject(url, rqDto, ChatResponseDto.class);
    }

    public SubscribeResponseDto subscribe(SubscribeRequestDto rqDto) {
        var endpoint = cfg.getHost() + ":" + cfg.getPort();
        var url = "http://" + endpoint + "/book-worm/" + rqDto.getUserId() + "/subscribe";
        return rest.postForObject(url, rqDto, SubscribeResponseDto.class);
    }

    public UnsubscribeResponseDto unsubscribe(UnsubscribeRequestDto rqDto) {
        var endpoint = cfg.getHost() + ":" + cfg.getPort();
        var url = "http://" + endpoint + "/book-worm/" + rqDto.getUserId() + "/unsubscribe";
        return rest.postForObject(url, rqDto, UnsubscribeResponseDto.class);
    }
}
