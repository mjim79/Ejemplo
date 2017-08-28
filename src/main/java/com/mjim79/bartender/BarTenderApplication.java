package com.mjim79.bartender;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.context.properties.*;

@SpringBootApplication
@EnableConfigurationProperties
public class BarTenderApplication {

    public static void main(String[] args) {
        SpringApplication.run(BarTenderApplication.class, args);
    }
}
