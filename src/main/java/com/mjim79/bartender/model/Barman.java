package com.mjim79.bartender.model;

import java.util.*;

public class Barman {

    private final Queue<DrinkType> drinksInProgress = new LinkedList<>();

    private BarmanStatus barmanStatus = BarmanStatus.FREE;

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

    public boolean canPrepareDrink(DrinkType drink) {
        return this.barmanStatus.equals(BarmanStatus.FREE)
                || this.barmanStatus.equals(BarmanStatus.PREPARING_ONE_BEER) && DrinkType.BEER.equals(drink);
    }

    public BarmanStatus getBarmanStatus() {
        return barman.barmanStatus;
    }

    public void updateStatus() {
        if (barman.drinksInProgress.isEmpty()) {
            this.barmanStatus = BarmanStatus.FREE;

        } else if (barman.drinksInProgress.size() == 1 && DrinkType.BEER.equals(barman.drinksInProgress.peek())) {
            this.barmanStatus = BarmanStatus.PREPARING_ONE_BEER;

        } else if (barman.drinksInProgress.size() == 1 && DrinkType.DRINK.equals(barman.drinksInProgress.peek())) {
            this.barmanStatus = BarmanStatus.PREPARING_ONE_DRINK;

        } else if (barman.drinksInProgress.size() == 2 && DrinkType.BEER.equals(barman.drinksInProgress.peek())) {
            this.barmanStatus = BarmanStatus.PREPARING_TWO_BEERS;
        } else {
            throw new IllegalStateException("invalid.barman.status");
        }

    }

    public void acceptDrink(DrinkType drink) {
        this.drinksInProgress.add(drink);
        this.updateStatus();
    }

    public void removeDrink() {
        this.drinksInProgress.remove();
        this.updateStatus();
    }

    public DrinkType getDrinkInProgress() {
        return this.drinksInProgress.peek();
    }

}
