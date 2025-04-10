package org.vse.bookworm.processor.cmd;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum Command {
    START("/start", "Список команд."),
    LOGIN("/login", "Выслать код входа в приложение"),
    LOGOUT("/logout", "Выйти из приложения"),
    SUBSCRIBE("/subscribe", "Подписаться к группе"),
    UNSUBSCRIBE("/unsubscribe", "Отписаться от группы"),
    BUILD("/build", "Собрать сообщения в книгу fb2");

    private final String text;
    private final String info;
    private static final Map<String, Command> textVsCmd;

    static {
        textVsCmd = Arrays.stream(values())
                .collect(Collectors.toMap(Command::text, x -> x));
    }

    Command(String text, String info) {
        this.text = text;
        this.info = info;

    }

    public String text() {
        return text;
    }

    public String info() {
        return info;
    }

    @Nullable
    public static Command ofText(@NotNull String text) {
        return textVsCmd.get(text);
    }
}
