## BarTender service ##

The BarTender service has been implemented using Spring Boot, Maven, Java 8 and Lombok. The service implements one controller which attends POST and GET requests in the URL: http://localhost:8080/bartender.

The optional part has been implemented.  You can change the seconds to prepare a drink and to wait for the barman is free in the *application.properties* file:

```properties
secondsToPrepareDrink=5
secondsToWaitForTheBarman=2
```

### Technical implementation ###
I try to do the service as simple as I can.  I would like to do only three comments about the implementation:
#####1. Singleton#####
I've implemented the Barman class as a singleton. We can only have one barman in the system, so we only can have one status (free, preparing drinks...) and one list of drinks being prepared at once.
#####2. CompletableFuture#####
Launching the order to prepare a drink I've used *java.util.concurrent.CompletableFuture* (since Java 1.8).  *CompletableFuture.supplyAsync* launch an async task in a new thread (by default in ForkJoinPool.commonPool()) and return a result when finished.  *CompletableFuture.thenAccept* execute a callback to notify the end of the thread execution.
```java
    private void startToPrepareDrink() {
        CompletableFuture.supplyAsync(this::doPrepareDrink).thenAccept(this::notifyDrinkReady);
    }
```
#####3. CountDownLatch#####
To implement the wait up to time for the barman is free, I've used *java.util.concurrent.CountDownLatch* (since Java 1.7).  I've launch a runnable in a new thread using a single thread executor (*java.util.concurrent.ExecutorService*) passing throught the CountDownLatch to count down when the barman is free, while the main thread wait up to the seconds specified in the configuration.
```java
    private boolean waitForTheBarman(DrinkType drink) {

        ExecutorService service = null;

        try {

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
        while (!thcis.barmanService.canPrepareDrink(drink)) {
            try {
                Thread.sleep(500);
            } catch (final InterruptedException e) {
                this.handleInterruptedExceptionWaitingForTheBarmaIsFree(e);
            }
        }
        done.countDown();
    }
```



### JSON to POST a new order: ###
```json
{
    "customer": "28",
    "drink": "drink"
}
```

#### GET request to get the orders listed - JSON group by customer: ####
```json
{
    "Customer(id=16, name=Customer 16)": {
        "beer": 4,
        "drink": 3
    },
    "Customer(id=28, name=Customer 28)": {
        "beer": 5,
        "drink": 2
    },
    "Customer(id=56, name=Customer 56)": {
        "beer": 1,
        "drink": 0
    }
}
```
