package org.vse.bookworm.telegram.utils;

import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.jetbrains.annotations.NotNull;
import org.vse.bookworm.dto.kafka.ChatDto;
import org.vse.bookworm.dto.kafka.FileMessageDto;
import org.vse.bookworm.dto.kafka.TextMessageDto;
import org.vse.bookworm.dto.kafka.UserDto;
import org.vse.bookworm.utils.Asserts;

public final class TlgDto {
    private TlgDto() {
    }

    @NotNull
    public static TextMessageDto textMessage(@NotNull Update upd) {
        var msg = Asserts.notNull(upd.message(), "Update.message");
        var chat = chat(msg);
        return TextMessageDto.builder()
                .setText(msg.text())
                .setChat(chat(msg))
                .setSender(user(msg))
                .setMessageId(msg.messageId())
                .setAffinityKey(chat.getId())
                .build();
    }

    @NotNull
    public static FileMessageDto fileMessage(@NotNull Update upd, @NotNull File file) {
        var msg = Asserts.notNull(upd.message(), "Update.message");
        var doc = Asserts.notNull(msg.document(), "Update.message.document");
        var chat = chat(msg);

        return FileMessageDto.builder()
                .setFileId(doc.fileId())
                .setFileUniqueId(doc.fileUniqueId())
                .setFileSize(Asserts.notNull(file.fileSize(), "fileSize"))
                .setFilename(doc.fileName())
                .setCaption(msg.caption())
                .setMessageId(msg.messageId())
                .setChat(chat)
                .setUser(user(msg))
                .setAffinityKey(chat.getId())
                .build();
    }

    @NotNull
    public static UserDto user(@NotNull Message msg) {
        var usr = Asserts.notNull(msg.from(), "Message.from");

        return  UserDto.builder()
                .setFirstName(usr.firstName())
                .setLastName(usr.lastName())
                .setUserName(usr.username())
                .setId(usr.id())
                .build();
    }

    @NotNull
    public static ChatDto chat(@NotNull Message msg) {
        var chat = Asserts.notNull(msg.chat(), "Message.chat");
        return ChatDto.builder()
                .setFirstName(chat.firstName())
                .setLastName(chat.lastName())
                .setUserName(chat.username())
                .setId(chat.id())
                .setType(chat.type().name())
                .setTitle(chat.title())
                .build();
    }
}
