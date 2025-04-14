package org.vse.bookworm.service;

import org.vse.bookworm.dto.internal.MessageSaveRequestDto;
import org.vse.bookworm.dto.internal.MessageSaveResponseDto;
import org.vse.bookworm.dto.internal.DeleteMessageRequestDto;
import org.vse.bookworm.dto.internal.DeleteMessageResponseDto;

public interface ChatMessageService {
    MessageSaveResponseDto saveMessage(MessageSaveRequestDto requestDto);

    DeleteMessageResponseDto deleteMessage(DeleteMessageRequestDto requestDto);
}
