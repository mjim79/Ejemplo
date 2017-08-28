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

    private static final String MSG_INFO_NEW_REQUEST_ACCEPT_ORDER = " --> NEW post request to ACCEPT order {}";

    private static final String MSG_INFO_NEW_REQUEST_LIST_ORDERS = " --> NEW get request to get the orders LIST";

    private static final String MSG_INFO_REQUEST_ACCEPTED_FOR_THE_ORDER = " --> Request ACCEPTED for the order {}";

    private static final String MSG_INFO_REQUEST_REFUSED_FOR_THE_ORDER = " --> Request REFUSED for the order {}";

    private static final String MSG_INFO_REQUEST_COMPLETED_GET_LIST_ORDERS = " --> Get request to get the oders list COMPLETED";

    private final BarTenderService barTenderService;

    private final OrderValidator orderValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(this.orderValidator);
    }

    @RequestMapping(value = "/bartender", method = RequestMethod.POST)
    public ResponseEntity<?> acceptOrder(@Valid @RequestBody Order order) {

        LOGGER.info(MSG_INFO_NEW_REQUEST_ACCEPT_ORDER, order);
        try {
            if (this.barTenderService.acceptOrder(order)) {
                LOGGER.info(MSG_INFO_REQUEST_ACCEPTED_FOR_THE_ORDER, order);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                LOGGER.info(MSG_INFO_REQUEST_REFUSED_FOR_THE_ORDER, order);
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
            LOGGER.info(MSG_INFO_NEW_REQUEST_LIST_ORDERS, Thread.currentThread().getName());
            final Map<Customer, EnumMap<DrinkType, Integer>> orders = this.barTenderService.listOrders();
            LOGGER.info(MSG_INFO_REQUEST_COMPLETED_GET_LIST_ORDERS);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (final Exception e) {
            LOGGER.error(MSG_ERROR_LIST_ORDERS, e);
            throw new BarTenderException(MSG_ERROR_LIST_ORDERS, e);
        }

    }

}
