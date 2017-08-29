package com.mjim79.bartender.services;

import java.util.*;
import java.util.concurrent.*;

import org.springframework.stereotype.*;

import com.mjim79.bartender.model.*;
import com.mjim79.bartender.repositories.*;

import lombok.*;
import lombok.extern.slf4j.*;

@Service
@AllArgsConstructor
@Slf4j
public class BarTenderService {

    private static final String MSG_ERR_WAITING_FOR_THE_BARMAN = "error.waiting.for.the.barman";

    private final BarTenderRepository barTenderRepository;

    private final BarmanService barmanService;

    private final BarTenderConfiguration barTenderConfiguration;

    public boolean acceptOrder(Order order) {

        if (!this.barmanService.canPrepareDrink(order.getDrink()) && !this.waitForTheBarman(order.getDrink())) {
            return false;
        }

        if (this.barmanService.prepareDrink(order.getDrink())) {
            this.barTenderRepository.save(order);
            return true;
        }

        return false;
    }

    private boolean waitForTheBarman(DrinkType drink) {

        ExecutorService service = null;

        try {
            // java.util.concurrent.CountDownLatch -> Since Java 1.7.
            // CountDownLatch - await , wait for the count down to 0 or a time out.
            final CountDownLatch done = new CountDownLatch(1);
            service = Executors.newSingleThreadExecutor();
            service.execute(() -> this.waitForTheBarmanFree(drink, done));

            return done.await(this.barTenderConfiguration.getSecondsToWaitForTheBarman(), TimeUnit.SECONDS);

        } catch (final InterruptedException e) {
            this.handleInterruptedExceptionWaitingForTheBarmaIsFree(e);
        } finally {
            if (Objects.nonNull(service)) {
                service.shutdown();
            }
        }

        return false;
    }

    private void waitForTheBarmanFree(DrinkType drink, CountDownLatch done) {
        while (!this.barmanService.canPrepareDrink(drink)) {
            try {
                Thread.sleep(500);
            } catch (final InterruptedException e) {
                this.handleInterruptedExceptionWaitingForTheBarmaIsFree(e);
            }
        }
        done.countDown();
    }

    private void handleInterruptedExceptionWaitingForTheBarmaIsFree(final InterruptedException e) {
        log.error(MSG_ERR_WAITING_FOR_THE_BARMAN, e);
        Thread.currentThread().interrupt();
        throw new BarTenderException(MSG_ERR_WAITING_FOR_THE_BARMAN, e);
    }

    public Map<Customer, EnumMap<DrinkType, Integer>> listOrders() {
        return this.barTenderRepository.findAll();
    }

}
