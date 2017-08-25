package com.mjim79.bartender.model;

import lombok.*;

@Data
@AllArgsConstructor
public class Order {

    private Long customer;

    private DrinkType drink;

}
