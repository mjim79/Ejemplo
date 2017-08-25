package com.mjim79.bartender.controller;

import java.util.*;

import javax.validation.*;

import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;

import com.mjim79.bartender.model.*;
import com.mjim79.bartender.services.*;

import lombok.*;

@RestController
@AllArgsConstructor
public class BarTenderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BarTenderController.class);

    private static final String MSG_ERROR_ACCEPT_ORDER = "error.accept.order";

    private static final String MSG_ERROR_LIST_ORDERS = "error.list.order";

    private final BarTenderService barTenderService;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new OrderValidator());
    }

    @RequestMapping(value = "/bartender", method = RequestMethod.POST)
    public ResponseEntity<?> acceptOrder(@Valid @RequestBody Order order) {

        try {
            if (this.barTenderService.acceptOrder(order)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
            }
        } catch (final Exception e) {
            LOGGER.error(MSG_ERROR_ACCEPT_ORDER, e);
            throw new BarTenderException(MSG_ERROR_ACCEPT_ORDER, e);

        }
    }

    @RequestMapping(value = "/bartender", method = RequestMethod.GET)
    public ResponseEntity<?> listOrders() {

        try {
            final Map<Customer, EnumMap<DrinkType, Integer>> orders = this.barTenderService.listOrders();
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (final Exception e) {
            LOGGER.error(MSG_ERROR_LIST_ORDERS, e);
            throw new BarTenderException(MSG_ERROR_LIST_ORDERS, e);
        }

    }

}
