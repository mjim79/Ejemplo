package com.mjim79.bartender.model;

import java.util.*;

import javax.annotation.*;

import org.springframework.boot.context.properties.*;
import org.springframework.context.annotation.*;

import lombok.*;

@Configuration
@ConfigurationProperties
@Data
public class BarTenderConfiguration {

    private static final Integer SECONDS_TO_PREPARE_DRINK_DEFAULT = 5;

    private static final Integer SECONDS_TO_WAIT_FOR_THE_BARMAN_DEFAULT = 2;

    private Integer secondsToPrepareDrink;

    private Integer secondsToWaitForTheBarman;

    @PostConstruct
    public void setDefaultValues() {
        if (Objects.isNull(this.secondsToPrepareDrink)) {
            this.secondsToPrepareDrink = SECONDS_TO_PREPARE_DRINK_DEFAULT;
        }
        if (Objects.isNull(this.secondsToWaitForTheBarman)) {
            this.secondsToWaitForTheBarman = SECONDS_TO_WAIT_FOR_THE_BARMAN_DEFAULT;
        }
    }

}
