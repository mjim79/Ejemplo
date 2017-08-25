package com.mjim79.bartender.model;

import java.util.*;

import com.fasterxml.jackson.annotation.*;

public enum DrinkType {
    BEER("beer"), DRINK("drink");

    private final String value;

    DrinkType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }

    @JsonCreator
    public static DrinkType fromValue(String value) {

        final Optional<DrinkType> drinkType = Arrays.asList(DrinkType.values()).stream()
                .filter(v -> v.getValue().equalsIgnoreCase(value)).findAny();

        if (drinkType.isPresent()) {
            return drinkType.get();
        } else {
            throw new IllegalArgumentException("Invalid value for drink type: DRINK | BEER");
        }

    }

}
