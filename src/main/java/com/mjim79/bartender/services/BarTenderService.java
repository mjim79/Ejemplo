package com.mjim79.bartender.services;

import java.util.*;

import org.springframework.stereotype.*;

import com.mjim79.bartender.model.*;
import com.mjim79.bartender.repositories.*;

import lombok.*;

@Service
@AllArgsConstructor
public class BarTenderService {

    private final BarTenderRepository barTenderRepository;

    public boolean acceptOrder(Order order) {
        this.barTenderRepository.save(order);
        return true;
    }

    public Map<Customer, EnumMap<DrinkType, Integer>> listOrders() {
        return this.barTenderRepository.findAll();
    }

}
