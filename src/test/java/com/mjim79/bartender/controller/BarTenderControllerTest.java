package com.mjim79.bartender.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.*;
import org.springframework.http.*;
import org.springframework.web.bind.*;

import com.mjim79.bartender.model.*;
import com.mjim79.bartender.services.*;

public class BarTenderControllerTest {

    private static final Order ORDER = new Order(13L, DrinkType.BEER);

    private static final Map<Customer, EnumMap<DrinkType, Integer>> ORDERS = new HashMap<>();

    private BarTenderController controller;

    private final BarTenderService barTenderService = mock(BarTenderService.class);

    private final OrderValidator orderValidator = mock(OrderValidator.class);

    @Before
    public void init() {
        this.controller = new BarTenderController(this.barTenderService, this.orderValidator);
    }

    @Test
    public void shouldReturnOKWhenOrderIsAcepted() {

        when(this.barTenderService.acceptOrder(ORDER)).thenReturn(true);

        final ResponseEntity<?> result = this.controller.acceptOrder(ORDER);

        assertThat(result.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void shouldReturnTooManyRequestsWhenOrderIsNotAccpeted() {

        when(this.barTenderService.acceptOrder(ORDER)).thenReturn(false);

        final ResponseEntity<?> result = this.controller.acceptOrder(ORDER);

        assertThat(result.getStatusCode(), is(HttpStatus.TOO_MANY_REQUESTS));
    }

    @Test(expected = BarTenderException.class)
    public void shouldThrowABarTenderExceptionWhenAnyExceptionIsThrowedAcceptingOrder() {

        doThrow(RuntimeException.class).when(this.barTenderService).acceptOrder(ORDER);

        this.controller.acceptOrder(ORDER);
    }

    @Test
    public void shouldReturnOKAndTheOrdersRegisteredWhenAskForTheListOrders() {

        when(this.barTenderService.listOrders()).thenReturn(ORDERS);

        final ResponseEntity<?> result = this.controller.listOrders();

        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody(), is(ORDERS));

    }

    @Test(expected = BarTenderException.class)
    public void shouldThrowABarTenderExceptionWhenAnyExceptionIsThrowedListingOrders() {

        doThrow(RuntimeException.class).when(this.barTenderService).listOrders();

        this.controller.listOrders();
    }

    @Test
    public void shouldSetValidatorForTheBinder() {

        final WebDataBinder binder = mock(WebDataBinder.class);

        this.controller.initBinder(binder);

        verify(binder).setValidator(this.orderValidator);

    }
}
