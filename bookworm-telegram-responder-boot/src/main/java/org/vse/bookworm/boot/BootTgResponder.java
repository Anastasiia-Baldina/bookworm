package org.vse.bookworm.boot;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class BootTgResponder {
    public static void main(String[] args) {
        new SpringApplicationBuilder(BootTgResponder.class)
                .web(WebApplicationType.NONE )
                .run(args);
    }
}
