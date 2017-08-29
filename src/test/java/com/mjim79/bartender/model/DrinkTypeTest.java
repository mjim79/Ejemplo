package com.mjim79.bartender.model;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.*;

public class DrinkTypeTest {

    @Test
    public void shouldGetTheDrinkTypeValue() {

        assertThat(DrinkType.BEER.getValue(), is("beer"));
    }

    @Test
    public void shouldGetDrinkTypeFromValue() {

        assertThat(DrinkType.fromValue("drink").equals(DrinkType.DRINK), is(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumenExceptionIfIsNotAValueForDrinkType() {

        DrinkType.fromValue("invalid value");
    }

}
