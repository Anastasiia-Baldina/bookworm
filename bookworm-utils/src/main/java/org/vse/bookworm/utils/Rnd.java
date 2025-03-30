package org.vse.bookworm.utils;

import java.time.Instant;
import java.util.Random;

public final class Rnd {
    private static final Random RANDOM = new Random(Instant.now().toEpochMilli());

    private Rnd() {
        //no-op
    }

    public static int nextInt() {
        return RANDOM.nextInt();
    }
}
