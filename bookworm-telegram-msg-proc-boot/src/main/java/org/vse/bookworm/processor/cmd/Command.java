package org.vse.bookworm.processor.cmd;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum Command {
    START("/start", "Список команд", true, 0, "/start"),
    LOGIN("/login", "Выслать код входа в приложение", false, 0, "/login"),
    LOGOUT("/logout", "Выйти из приложения", false, 0, "/logout"),
    SUBSCRIBE("/subscribe", "Подписаться к группе", false, 1,
            "/subscribe https://t.me/testmybotkyrsach"),
    UNSUBSCRIBE("/unsubscribe", "Отписаться от группы", false, 1,
            "/unsubscribe https://t.me/testmybotkyrsach"),
    BUILD("/build", "Собрать сообщения в книгу fb2", true, 1,
            "/build #хештэг\n#хештэг - хештэг, указанный в сообщениях, по которому собирается книга.");

    private final String text;
    private final String info;
    private final boolean inGroup;
    private final int argCount;
    private final String usage;
    private static final Map<String, Command> textVsCmd;

    static {
        textVsCmd = Arrays.stream(values())
                .collect(Collectors.toMap(Command::text, x -> x));
    }

    Command(String text, String info, boolean inGroup, int argCount, String usage) {
        this.text = text;
        this.info = info;
        this.inGroup = inGroup;
        this.argCount = argCount;
        this.usage = usage;
    }

    public String text() {
        return text;
    }

    public String info() {
        return info;
    }

    public boolean inGroup() {
        return inGroup;
    }

    public String usage() {
        return usage;
    }

    public int argCount() {
        return argCount;
    }

    @Nullable
    public static Command ofText(@NotNull String text) {
        return textVsCmd.get(text);
    }
}
