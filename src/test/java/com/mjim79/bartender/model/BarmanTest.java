package com.mjim79.bartender.model;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.*;

public class BarmanTest {

    private Barman barman;

    @Before
    public void init() {
        this.barman = Barman.getInstance();
        while (!BarmanStatus.FREE.equals(this.barman.getBarmanStatus())) {
            this.barman.removeDrink();
        }
    }

    @Test
    public void shouldGetTheSameBarmanInstance() {

        final Barman barman2 = Barman.getInstance();

        assertThat(barman2, is(sameInstance(this.barman)));

    }

    @Test
    public void shouldAcceptNewDrinkWhenBarmanIsFree() {

        this.barman.acceptDrink(DrinkType.DRINK);

        assertThat(this.barman.getBarmanStatus(), is(BarmanStatus.PREPARING_ONE_DRINK));
        assertThat(this.barman.getDrinkInProgress(), is(DrinkType.DRINK));

    }

    @Test
    public void shouldAcceptNewBeerWhenBarmanIsPreparingOneBeer() {

        this.barman.acceptDrink(DrinkType.BEER);
        this.barman.acceptDrink(DrinkType.BEER);

        assertThat(this.barman.getBarmanStatus(), is(BarmanStatus.PREPARING_TWO_BEERS));
        assertThat(this.barman.getDrinkInProgress(), is(DrinkType.BEER));

    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotAcceptNewDrinkWhenBarmanIsPreparinOneDrink() {

        this.barman.acceptDrink(DrinkType.DRINK);
        this.barman.acceptDrink(DrinkType.DRINK);
    }

    @Test
    public void shouldReturnTrueAskingForPrepareDrinkWhenBarmanIsFree() {

        final Boolean result = this.barman.canPrepareDrink(DrinkType.DRINK);

        assertThat(result, is(true));

    }

    @Test
    public void shouldReturnFalseAskingForPrepareDrinkWhenBarmanIsPreparingABeer() {

        this.barman.acceptDrink(DrinkType.BEER);

        final Boolean result = this.barman.canPrepareDrink(DrinkType.DRINK);

        assertThat(result, is(false));

    }

    @Test
    public void shouldReturnTrueAskingForPrepareBeerWhenBarmanIsPreparingABeer() {

        this.barman.acceptDrink(DrinkType.BEER);

        final Boolean result = this.barman.canPrepareDrink(DrinkType.BEER);

        assertThat(result, is(true));

    }

}
