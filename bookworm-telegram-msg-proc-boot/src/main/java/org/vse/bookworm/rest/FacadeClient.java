package org.vse.bookworm.rest;

import org.springframework.web.client.RestTemplate;
import org.vse.bookworm.dto.internal.ChatJoinRequestDto;
import org.vse.bookworm.dto.internal.ChatJoinResponseDto;
import org.vse.bookworm.dto.internal.ChatRequestDto;
import org.vse.bookworm.dto.internal.ChatResponseDto;
import org.vse.bookworm.dto.internal.LoginRequestDto;
import org.vse.bookworm.dto.internal.LoginResponseDto;
import org.vse.bookworm.dto.internal.MessageListRequestDto;
import org.vse.bookworm.dto.internal.MessageListResponseDto;
import org.vse.bookworm.dto.internal.MessageSaveRequestDto;
import org.vse.bookworm.dto.internal.MessageSaveResponseDto;
import org.vse.bookworm.dto.internal.SubscribeRequestDto;
import org.vse.bookworm.dto.internal.SubscribeResponseDto;
import org.vse.bookworm.dto.internal.UnsubscribeRequestDto;
import org.vse.bookworm.dto.internal.UnsubscribeResponseDto;
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

    public ChatJoinResponseDto joinChat(ChatJoinRequestDto rqDto) {
        var endpoint = cfg.getHost() + ":" + cfg.getPort();
        var url = "http://" + endpoint + "/book-worm/" + rqDto.getChatName().hashCode() + "/add_chat";
        return rest.postForObject(url, rqDto, ChatJoinResponseDto.class);
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

    public MessageSaveResponseDto saveMessage(MessageSaveRequestDto rqDto) {
        var endpoint = cfg.getHost() + ":" + cfg.getPort();
        var url = "http://" + endpoint + "/book-worm/" + rqDto.getChatId() + "/message/save";
        return rest.postForObject(url, rqDto, MessageSaveResponseDto.class);
    }

    public MessageSaveResponseDto deleteMessage(MessageSaveRequestDto rqDto) {
        var endpoint = cfg.getHost() + ":" + cfg.getPort();
        var url = "http://" + endpoint + "/book-worm/" + rqDto.getChatId() + "/message/delete";
        return rest.postForObject(url, rqDto, MessageSaveResponseDto.class);
    }
}
