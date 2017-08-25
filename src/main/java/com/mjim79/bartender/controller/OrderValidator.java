package com.mjim79.bartender.controller;

import java.util.*;

import org.springframework.stereotype.*;
import org.springframework.validation.*;

import com.mjim79.bartender.model.*;

import lombok.*;

@AllArgsConstructor
@Component
public class OrderValidator implements Validator {

    @Override

    public boolean supports(Class<?> clazz) {
        return Order.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors e) {

        ValidationUtils.rejectIfEmpty(e, "customer", "customer.empty");
        ValidationUtils.rejectIfEmpty(e, "drink", "drink.empty");

        final Order order = (Order)target;

        if (Objects.isNull(order)) {
            e.rejectValue("order", "order.not.valid");
            return;
        }

        if (Objects.isNull(order.getDrink()) || !(order.getDrink() instanceof DrinkType)) {
            e.rejectValue("order", "drink.type..not.valid");
        }

    }

}
