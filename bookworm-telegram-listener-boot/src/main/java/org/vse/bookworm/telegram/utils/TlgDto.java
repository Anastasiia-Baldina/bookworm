package org.vse.bookworm.telegram.utils;

import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.jetbrains.annotations.NotNull;
import org.vse.bookworm.dto.kafka.Chat;
import org.vse.bookworm.dto.kafka.FileMessage;
import org.vse.bookworm.dto.kafka.TextMessage;
import org.vse.bookworm.dto.kafka.User;
import org.vse.bookworm.utils.Asserts;

public final class TlgDto {
    private TlgDto() {
    }

    @NotNull
    public static TextMessage textMessage(@NotNull Update upd) {
        var msg = Asserts.notNull(upd.message(), "Update.message");
        var chat = chat(msg);
        return TextMessage.builder()
                .setText(msg.text())
                .setChat(chat(msg))
                .setSender(user(msg))
                .setMessageId(msg.messageId())
                .setAffinityKey(chat.getId())
                .build();
    }

    @NotNull
    public static FileMessage fileMessage(@NotNull Update upd, @NotNull File file) {
        var msg = Asserts.notNull(upd.message(), "Update.message");
        var doc = Asserts.notNull(msg.document(), "Update.message.document");
        var chat = chat(msg);

        return FileMessage.builder()
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
    public static User user(@NotNull Message msg) {
        var usr = Asserts.notNull(msg.from(), "Message.from");

        return  User.builder()
                .setFirstName(usr.firstName())
                .setLastName(usr.lastName())
                .setUserName(usr.username())
                .setId(usr.id())
                .build();
    }

    @NotNull
    public static Chat chat(@NotNull Message msg) {
        var chat = Asserts.notNull(msg.chat(), "Message.chat");
        return Chat.builder()
                .setFirstName(chat.firstName())
                .setLastName(chat.lastName())
                .setUserName(chat.username())
                .setId(chat.id())
                .setType(chat.type().name())
                .setTitle(chat.title())
                .build();
    }
}
