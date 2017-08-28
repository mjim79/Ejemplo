package com.mjim79.bartender.model;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.*;

public class BarTenderConfigurationTest {

    final BarTenderConfiguration configuration = new BarTenderConfiguration();

    private static final Integer SECONDS_TO_PREPARE_DRINK_DEFAULT = 5;

    private static final Integer SECONDS_TO_WAIT_FOR_THE_BARMAN_DEFAULT = 2;

    @Test
    public void shouldSetDefaultValuesIfMissing() {

        this.configuration.setDefaultValues();

        assertThat(this.configuration.getSecondsToPrepareDrink(), is(SECONDS_TO_PREPARE_DRINK_DEFAULT));
        assertThat(this.configuration.getSecondsToWaitForTheBarman(), is(SECONDS_TO_WAIT_FOR_THE_BARMAN_DEFAULT));
    }

    @Test
    public void shouldNotSetDefaultValuesIfPresent() {

        this.configuration.setSecondsToPrepareDrink(100);
        this.configuration.setSecondsToWaitForTheBarman(50);

        this.configuration.setDefaultValues();

        assertThat(this.configuration.getSecondsToPrepareDrink(), is(100));
        assertThat(this.configuration.getSecondsToWaitForTheBarman(), is(50));
    }
}
