package org.vse.bookworm.service;

import org.jetbrains.annotations.NotNull;

public record Host(@NotNull String address, int port) {
    @NotNull
    public String endpoint() {
        return "http://" + address + ':' + port;
    }
}
