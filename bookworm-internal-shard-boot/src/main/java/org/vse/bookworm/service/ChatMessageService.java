package org.vse.bookworm.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.vse.bookworm.dto.internal.*;

public interface ChatMessageService {
    MessageSaveResponseDto saveMessage(MessageSaveRequestDto requestDto);

    DeleteMessageResponseDto deleteMessage(DeleteMessageRequestDto requestDto);

    MessageListResponseDto listMessage(MessageListRequestDto requestDto);
}
