package com.mjim79.bartender.services;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.*;

import com.mjim79.bartender.model.*;

public class BarmanServiceTest {

    private BarmanService service;

    private final BarTenderConfiguration barTenderConfiguration = mock(BarTenderConfiguration.class);

    private final Barman barman = Barman.getInstance();

    @Before
    public void init() {

        this.service = new BarmanService(this.barTenderConfiguration);
        while (!BarmanStatus.FREE.equals(this.barman.getBarmanStatus())) {
            this.barman.removeDrink();
        }
    }

    @Test
    public void shouldReturnTrueWhenBarmanIsFree() {

        final boolean result = this.service.canPrepareDrink(DrinkType.BEER);

        assertThat(result, is(true));

    }

    @Test
    public void shouldReturnFalseWhenBarmanIsPreparinADrink() {

        this.barman.acceptDrink(DrinkType.DRINK);

        final boolean result = this.service.canPrepareDrink(DrinkType.DRINK);

        assertThat(result, is(false));

    }

    @Test
    public void shouldPrepareDrink() {

        final boolean result = this.service.prepareDrink(DrinkType.BEER);

        assertThat(result, is(true));

    }

    @Test
    public void shouldReturnFalseWhenNotPrepareDrink() {

        this.barman.acceptDrink(DrinkType.DRINK);

        final boolean result = this.service.prepareDrink(DrinkType.BEER);

        assertThat(result, is(false));

    }

}
