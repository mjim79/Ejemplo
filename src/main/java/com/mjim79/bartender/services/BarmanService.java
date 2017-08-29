package com.mjim79.bartender.services;

import java.util.concurrent.*;

import org.springframework.stereotype.*;

import com.mjim79.bartender.model.*;

import lombok.*;
import lombok.extern.slf4j.*;

@Service
@AllArgsConstructor
@Slf4j
public class BarmanService {

    private static final String MSG_INFO_PREPARING_DRINK = "      --> Barman says: Preparing drink: ";

    private static final String MSG_INFO_DRINK_PREPARED = "      --> Barman says: Drink prerared: ";

    private static final String MSG_ERR_NO_DRINKS_IN_PROCESS = "no.drinks.are.being.prepared";

    private final BarTenderConfiguration barTenderConfiguration;

    private final Barman barman = Barman.getInstance();

    public boolean canPrepareDrink(DrinkType drink) {
        return this.barman.canPrepareDrink(drink);
    }

    public boolean prepareDrink(DrinkType drink) {

        if (this.barman.canPrepareDrink(drink)) {
            synchronized (this) {
                if (this.barman.canPrepareDrink(drink)) {
                    this.barman.acceptDrink(drink);
                    this.startToPrepareDrink();
                    return true;
                }
            }
        }

        return false;
    }

    private void startToPrepareDrink() {
        // java.util.concurrent.CompletableFuture --> since Java 1.8
        // Launch an async task in a new thread (ForkJoinPool.commonPool()) to prepare the drink, and then execute the
        // callback to notify the drink is prepared.
        CompletableFuture.supplyAsync(this::doPrepareDrink).thenAccept(this::notifyDrinkReady);

    }

    private String doPrepareDrink() {

        final String text = MSG_INFO_PREPARING_DRINK + this.barman.getDrinkInProgress();
        log.info(text);

        try {

            Thread.sleep(this.barTenderConfiguration.getSecondsToPrepareDrink() * 1000L);
            return MSG_INFO_DRINK_PREPARED + this.barman.getDrinkInProgress();

        } catch (final InterruptedException e) {
            log.error("Error " + text, e);
            Thread.currentThread().interrupt();
            throw new BarTenderException("Error " + text, e);
        }

    }

    private void notifyDrinkReady(String text) {

        if (BarmanStatus.FREE.equals(this.barman.getBarmanStatus())) {
            throw new IllegalStateException(MSG_ERR_NO_DRINKS_IN_PROCESS);
        }

        this.barman.removeDrink();
        log.info(text);
    }

}
