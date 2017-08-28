package com.mjim79.bartender.services;

import java.util.*;
import java.util.concurrent.*;

import org.slf4j.*;
import org.springframework.stereotype.*;

import com.mjim79.bartender.controller.*;
import com.mjim79.bartender.model.*;
import com.mjim79.bartender.repositories.*;

import lombok.*;

@Service
@AllArgsConstructor
public class BarTenderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BarTenderController.class);

    private final BarTenderRepository barTenderRepository;

    private final BarmanService barmanService;

    private final BarTenderConfiguration barTenderConfiguration;

    public boolean acceptOrder(Order order) {

        if (!this.barmanService.isFree(order.getDrink()) && !this.waitForTheBarman(order.getDrink())) {
            return false;
        }

        if (this.barmanService.prepareDrink(order.getDrink())) {
            this.barTenderRepository.save(order);
            return true;
        } else {
            return false;
        }

    }

    private boolean waitForTheBarman(DrinkType drink) {

        final CountDownLatch done = new CountDownLatch(1);

        ExecutorService service = null;

        try {

            service = Executors.newSingleThreadExecutor();
            service.execute(() -> this.waitForTheBarmanFree(drink, done));

            return done.await(this.barTenderConfiguration.getSecondsToWaitForTheBarman(), TimeUnit.SECONDS);

        } catch (final InterruptedException e) {
            this.manageInterruptedExceptionWaitingForTheBarmaIsFree(e);
        } finally {
            if (Objects.nonNull(service)) {
                service.shutdown();
            }
        }

        return false;
    }

    protected void manageInterruptedExceptionWaitingForTheBarmaIsFree(final InterruptedException e) {
        final String textError = "Error waiting for the barman...";
        LOGGER.error(textError, e);
        Thread.currentThread().interrupt();
        throw new BarTenderException(textError, e);
    }

    private void waitForTheBarmanFree(DrinkType drink, CountDownLatch done) {
        while (!this.barmanService.isFree(drink)) {
            try {
                Thread.sleep(500);
            } catch (final InterruptedException e) {
                this.manageInterruptedExceptionWaitingForTheBarmaIsFree(e);
            }
        }
        done.countDown();
    }

    public Map<Customer, EnumMap<DrinkType, Integer>> listOrders() {
        return this.barTenderRepository.findAll();
    }

}
