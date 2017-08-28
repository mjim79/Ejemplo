package com.mjim79.bartender.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.*;
import org.springframework.validation.*;

import com.mjim79.bartender.model.*;

public class OrderValidatorTest {

    private OrderValidator validator;

    @Before
    public void init() {
        this.validator = new OrderValidator();
    }

    @Test
    public void supports() {
        assertThat(this.validator.supports(Order.class), is(true));
        assertThat(this.validator.supports(Object.class), is(false));
    }

    @Test
    public void shouldValidateOK() {

        final Order order = new Order(45L, DrinkType.BEER);
        final BindException errors = new BindException(order, "order");

        this.validator.validate(order, errors);

        assertThat(errors.hasErrors(), is(false));

    }

    @Test
    public void shouldNotValidateIfTheCustomerIsMissing() {
        final Order order = new Order(null, DrinkType.BEER);
        final BindException errors = new BindException(order, "order");

        this.validator.validate(order, errors);

        assertThat(errors.hasErrors(), is(true));
        assertThat(errors.getFieldErrorCount("customer"), is(1));
    }

    @Test
    public void shouldNotValidateIfTheDrinkTypeIsMissing() {
        final Order order = new Order(44L, null);
        final BindException errors = new BindException(order, "order");

        this.validator.validate(order, errors);

        assertThat(errors.hasErrors(), is(true));
        assertThat(errors.getFieldErrorCount("drink"), is(1));
    }

}
