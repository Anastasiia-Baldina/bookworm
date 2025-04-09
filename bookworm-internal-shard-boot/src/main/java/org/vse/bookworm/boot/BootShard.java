package org.vse.bookworm.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class BootShard {
    public static void main(String[] args) {
        new SpringApplicationBuilder(BootShard.class)
                .run(args);
    }
}
