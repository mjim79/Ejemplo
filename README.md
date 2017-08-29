### Coding chanllenge: BarTender service ###

The BarTender service has been implemented accodring to the specifications provided by Knockout gaming using Spring Boot, Maven, Java 8, Lombok, Junit, Mockito and Hamcrest. The service implements one controller which attends POST and GET requests in the URL: http://localhost:8080/bartender.

The optional part has been implemented.  You can change the seconds to prepare a drink and to wait for the barman is free in the #application.properties# file:

```properties
secondsToPrepareDrink=5
secondsToWaitForTheBarman=2
```

# JSON to POST a new order: #
```json
{
    "customer": "28",
    "drink": "drink"
}
```

# GET request to get the orders listed - JSON group by customer: #
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