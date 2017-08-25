package com.mjim79.bartender.repositoies;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import com.mjim79.bartender.model.*;
import com.mjim79.bartender.repositories.*;

public class BarTenderRepositoryTest {

    private final BarTenderRepository repository = new BarTenderRepository();

    private static final String CUSTOMER_NAME_PREFIX = "Customer ";

    private static final Long CUSTOMER_ID = 24L;

    private static final Customer CUSTOMER = Customer.builder().id(CUSTOMER_ID).name(CUSTOMER_NAME_PREFIX + CUSTOMER_ID)
            .build();

    private static final Long NEW_CUSTOMER_ID = 32L;

    private static final Customer NEW_CUSTOMER = Customer.builder().id(NEW_CUSTOMER_ID)
            .name(CUSTOMER_NAME_PREFIX + NEW_CUSTOMER_ID).build();

    private static final Order ORDER = new Order(CUSTOMER_ID, DrinkType.BEER);

    @Test
    public void shouldSaveNewOrderWhenTheMapIsEmpty() {

        final Order result = this.repository.save(ORDER);

        assertThat(result, is(ORDER));
        final Map<Customer, EnumMap<DrinkType, Integer>> drinks = this.repository.findAll();
        assertThat(drinks.containsKey(CUSTOMER), is(true));
        assertThat(drinks.get(CUSTOMER).get(DrinkType.BEER), is(1));
        assertThat(drinks.get(CUSTOMER).get(DrinkType.DRINK), is(0));
    }

    @Test
    public void shouldSaveNewOrderWhenTheCustomerAlreadyExists() {

        this.repository.save(ORDER);

        final Order result = this.repository.save(ORDER);

        assertThat(result, is(ORDER));
        final Map<Customer, EnumMap<DrinkType, Integer>> drinks = this.repository.findAll();
        assertThat(drinks.containsKey(CUSTOMER), is(true));
        assertThat(drinks.get(CUSTOMER).get(DrinkType.BEER), is(2));
        assertThat(drinks.get(CUSTOMER).get(DrinkType.DRINK), is(0));

    }

    @Test
    public void shouldSaveNewOrderForNewCustomerWhenTheExistsOtherCustomers() {

        final Order orderCustomerNew = new Order(NEW_CUSTOMER_ID, DrinkType.DRINK);
        this.repository.save(ORDER);

        final Order result = this.repository.save(orderCustomerNew);

        assertThat(result, is(orderCustomerNew));
        final Map<Customer, EnumMap<DrinkType, Integer>> drinks = this.repository.findAll();
        assertThat(drinks.containsKey(CUSTOMER), is(true));
        assertThat(drinks.containsKey(NEW_CUSTOMER), is(true));
        assertThat(drinks.get(NEW_CUSTOMER).get(DrinkType.BEER), is(0));
        assertThat(drinks.get(NEW_CUSTOMER).get(DrinkType.DRINK), is(1));

    }

}
