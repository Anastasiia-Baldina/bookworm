package org.vse.bookworm.service;

import org.jetbrains.annotations.NotNull;

public interface Router {
    @NotNull
    Host route(Object key);
}
