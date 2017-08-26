package com.mjim79.bartender.services;

import java.util.*;
import java.util.concurrent.*;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;

import com.mjim79.bartender.controller.*;
import com.mjim79.bartender.model.*;

import lombok.*;

@Component
@Scope("singleton")
@Data
public class Barman {

    private static final Logger LOGGER = LoggerFactory.getLogger(BarTenderController.class);

    private final Queue<DrinkType> drinksInProgress = new LinkedList<>();

    @Value("${secondsToPrepareDrink:5}")
    private Integer secondsToPrepareDrink;

    private static Barman barman;

    private Barman() {

    }

    public static Barman getInstance() {
        if (Objects.isNull(barman)) {
            synchronized (Barman.class) {
                if (Objects.isNull(barman)) {
                    barman = new Barman();
                }
            }
        }

        return barman;

    }

    public boolean prepareDrink(DrinkType drink) {

        if (this.canPrepareDrink(drink)) {
            synchronized (Barman.class) {
                if (this.canPrepareDrink(drink)) {
                    this.drinksInProgress.add(drink);
                    this.startToPrepareDrink();
                    return true;
                } else {
                    return false;
                }

            }
        } else {
            return false;
        }

    }

    private void startToPrepareDrink() {

        CompletableFuture.supplyAsync(this::doPrepareDrink).thenAccept(this::notifyDrinkReady);

    }

    private void notifyDrinkReady(String text) {

        if (this.drinksInProgress.isEmpty()) {
            throw new IllegalStateException("No drinks are being prepared");
        }

        this.drinksInProgress.remove();
        LOGGER.info(text);
    }

    private String doPrepareDrink() {

        final String text = "Preparing drink " + this.drinksInProgress.peek();
        LOGGER.info(text);

        try {

            Thread.sleep(this.secondsToPrepareDrink * 1000L);
            return this.drinksInProgress.peek() + " prepared!";

        } catch (final InterruptedException e) {
            LOGGER.error("Error " + text, e);
            Thread.currentThread().interrupt();
            throw new BarTenderException("Error " + text, e);
        }

    }

    private boolean canPrepareDrink(DrinkType drink) {
        return this.drinksInProgress.isEmpty() || DrinkType.BEER.equals(drink) && this.isPreparingOnlyOneBeer();
    }

    private boolean isPreparingOnlyOneBeer() {
        return this.drinksInProgress.size() == 1 && DrinkType.BEER.equals(this.drinksInProgress.peek());
    }

}
