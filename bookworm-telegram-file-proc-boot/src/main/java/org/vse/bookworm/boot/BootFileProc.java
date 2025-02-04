package org.vse.bookworm.boot;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class BootFileProc {
    public static void main(String[] args) {
        new SpringApplicationBuilder(BootFileProc.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
