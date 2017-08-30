package com.mjim79.bartender.services;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.*;

import com.mjim79.bartender.model.*;
import com.mjim79.bartender.repositories.*;

public class BarTenderServiceTest {

    private static final Order BEER_ORDER = new Order(333L, DrinkType.BEER);

    private BarTenderService service;

    private final BarTenderRepository barTenderRepository = mock(BarTenderRepository.class);

    private final BarmanService barmanService = mock(BarmanService.class);

    private final BarTenderConfiguration barTenderConfiguration = mock(BarTenderConfiguration.class);

    @Before
    public void init() {
        this.service = new BarTenderService(this.barTenderRepository, this.barmanService, this.barTenderConfiguration);

    }

    @Test
    public void shouldAcceptOrderIfBarmanCanPrepareDrink() {

        when(this.barmanService.canPrepareDrink(DrinkType.BEER)).thenReturn(true);
        when(this.barmanService.prepareDrink(DrinkType.BEER)).thenReturn(true);

        final boolean result = this.service.acceptOrder(BEER_ORDER);

        assertThat(result, is(true));
        verify(this.barTenderRepository).save(BEER_ORDER);

    }

    @Test
    public void shouldNotAcceptOrderIfBarmanCanNotPrepareDrink() {

        when(this.barmanService.canPrepareDrink(DrinkType.BEER)).thenReturn(false);

        final boolean result = this.service.acceptOrder(BEER_ORDER);

        assertThat(result, is(false));

    }

    @Test
    public void shouldNotAcceptOrderIfBarmanCanNotPrepareDrinkAfterWaitForTheBarman() {

        when(this.barmanService.canPrepareDrink(DrinkType.BEER)).thenReturn(false, false, true);
        when(this.barTenderConfiguration.getSecondsToWaitForTheBarman()).thenReturn(1);

        final boolean result = this.service.acceptOrder(BEER_ORDER);

        assertThat(result, is(false));

    }

    @Test
    public void shouldAcceptOrderIfBarmanCanPrepareDrinkAfterWaitForTheBarman() {

        when(this.barmanService.canPrepareDrink(DrinkType.BEER)).thenReturn(false, true);
        when(this.barmanService.prepareDrink(DrinkType.BEER)).thenReturn(true);
        when(this.barTenderConfiguration.getSecondsToWaitForTheBarman()).thenReturn(1);

        final boolean result = this.service.acceptOrder(BEER_ORDER);

        assertThat(result, is(true));

    }

    @Test
    public void shouldNotAcceptOrderIfPrepareDrinkReturnsFalse() {

        when(this.barmanService.canPrepareDrink(DrinkType.BEER)).thenReturn(true);
        when(this.barmanService.prepareDrink(DrinkType.BEER)).thenReturn(false);

        final boolean result = this.service.acceptOrder(BEER_ORDER);

        assertThat(result, is(false));

    }

    @Test
    public void shouldGetOrders() {

        final Map<Customer, EnumMap<DrinkType, Integer>> orders = new HashMap<>();

        when(this.barTenderRepository.findAll()).thenReturn(orders);

        final Map<Customer, EnumMap<DrinkType, Integer>> result = this.service.listOrders();

        assertThat(result, is(orders));

    }

}
