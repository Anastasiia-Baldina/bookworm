package org.vse.bookworm.processor.cmd;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.GetChatMember;
import org.vse.bookworm.dto.internal.ChatRequestDto;
import org.vse.bookworm.dto.internal.SubscribeRequestDto;
import org.vse.bookworm.dto.kafka.TextMessageDto;
import org.vse.bookworm.dto.kafka.TextResponseDto;
import org.vse.bookworm.rest.FacadeClient;

public class CmdSubscribeHandler implements CommandHandler {
    private final FacadeClient facadeClient;
    private final TelegramBot bot;

    public CmdSubscribeHandler(FacadeClient facadeClient, TelegramBot bot) {
        this.facadeClient = facadeClient;
        this.bot = bot;
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
        var memRsp = bot.execute(new GetChatMember(rsp.getChatId(), userId));
        if (!memRsp.chatMember().isMember()) {
            responseBuilder(msg)
                    .setText("Вы не являетесь участником группы " + chatName)
                    .build();
        }
        var subRsp = facadeClient.subscribe(new SubscribeRequestDto()
                .setChatId(rsp.getChatId())
                .setUserId(userId)
                .setChatName(rsp.getChatName()));
        var rspText = subRsp.isSuccess()
                ? "Вы подписались на публикуемые в группу " + chatName + " книги"
                : "Ошибка выполнения. Попробуйте позже.";

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
        return Command.SUBSCRIBE;
    }
}
