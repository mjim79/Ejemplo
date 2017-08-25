package com.mjim79.bartender.model;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class Customer {

    private final Long id;

    private final String name;

}
