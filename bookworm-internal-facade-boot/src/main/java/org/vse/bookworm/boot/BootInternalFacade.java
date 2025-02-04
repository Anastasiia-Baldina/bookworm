package org.vse.bookworm.boot;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class BootInternalFacade {
    public static void main(String[] args) {
        new SpringApplicationBuilder(BootInternalFacade.class)
                .web(WebApplicationType.NONE )
                .run(args);
    }
}
