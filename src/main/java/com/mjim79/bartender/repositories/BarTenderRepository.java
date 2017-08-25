package com.mjim79.bartender.repositories;

import java.util.*;

import org.springframework.stereotype.*;

import com.mjim79.bartender.model.*;

@Component
public class BarTenderRepository {

    private final Map<Customer, EnumMap<DrinkType, Integer>> data = new HashMap<>();

    private static final String CUSTOMER_NAME_PREFIX = "Customer ";

    public Order save(Order order) {

        final Customer customer = Customer.builder().id(order.getCustomer())
                .name(CUSTOMER_NAME_PREFIX + order.getCustomer()).build();

        final EnumMap<DrinkType, Integer> updatedDrinks = this.getUpdatedDrinks(order, customer);

        this.data.put(customer, updatedDrinks);

        return order;

    }

    private EnumMap<DrinkType, Integer> getUpdatedDrinks(Order order, final Customer customer) {

        return this.data.containsKey(customer) ? this.addDrinkToDrinksMap(order.getDrink(), this.data.get(customer))
                : this.getInitializedDrinksMap(order);

    }

    private EnumMap<DrinkType, Integer> getInitializedDrinksMap(Order order) {

        final EnumMap<DrinkType, Integer> drinks = new EnumMap<>(DrinkType.class);
        drinks.put(DrinkType.BEER, DrinkType.BEER.equals(order.getDrink()) ? 1 : 0);
        drinks.put(DrinkType.DRINK, DrinkType.DRINK.equals(order.getDrink()) ? 1 : 0);
        return drinks;
    }

    private EnumMap<DrinkType, Integer> addDrinkToDrinksMap(DrinkType drink, EnumMap<DrinkType, Integer> drinks) {
        synchronized (this) {
            drinks.put(drink, drinks.get(drink) + 1);
        }

        return drinks;
    }

    public Map<Customer, EnumMap<DrinkType, Integer>> findAll() {
        return this.data;
    }

}
