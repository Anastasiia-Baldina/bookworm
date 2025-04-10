package org.vse.bookworm.processor.cmd;

import org.vse.bookworm.dto.internal.ChatRequestDto;
import org.vse.bookworm.dto.internal.SubscribeRequestDto;
import org.vse.bookworm.dto.internal.UnsubscribeRequestDto;
import org.vse.bookworm.dto.kafka.TextMessageDto;
import org.vse.bookworm.dto.kafka.TextResponseDto;
import org.vse.bookworm.rest.FacadeClient;

public class CmdUnsubscribeHandler implements CommandHandler {
    private final FacadeClient facadeClient;

    public CmdUnsubscribeHandler(FacadeClient facadeClient) {
        this.facadeClient = facadeClient;
    }

    @Override
    public TextResponseDto handle(TextMessageDto msg, String[] args) {
        String chatName = args[0];
        if (chatName.startsWith("https://t.me/")) {
            chatName = chatName.replace("https://t.me/", "");
        } else if (chatName.startsWith("t.me/")) {
            chatName = chatName.replace("t.me/", "");
        }
        var rsp = facadeClient.findChat(new ChatRequestDto().setChatName(chatName));
        if (!rsp.isSuccess()) {
            responseBuilder(msg)
                    .setText("Ошибка: группа не существует или к ней не подключен Telegram-бот.")
                    .build();
        }
        long userId = msg.getSender().getId();
        var unsRsp = facadeClient.unsubscribe(new UnsubscribeRequestDto()
                .setChatId(rsp.getChatId())
                .setUserId(userId));
        var rspText = unsRsp.isSuccess()
                ? "Вы больше не будете получить книги, опубликованные в группе " + chatName
                : "Ошибка выполнения. Попробуйте повторить позднее.";

        return responseBuilder(msg)
                .setText(rspText)
                .build();
    }

    private TextResponseDto.Builder responseBuilder(TextMessageDto msg) {
        return TextResponseDto.builder()
                .setAffinityKey(msg.getAffinityKey())
                .setChatId(msg.getChat().getId());
    }

    @Override
    public Command command() {
        return Command.UNSUBSCRIBE;
    }
}
