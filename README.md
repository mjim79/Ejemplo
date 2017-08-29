# README #

Coding chanllenge: BarTender service accodring to the specifications provided by Knockout gaming

### General specifications ###

The BarTender service has been implemented using Spring Boot, Maven, Java 8, Lombok, Junit, Mockito and Hamcrest.
The service implements one controller which attends POST and GET requests in the URL: http://localhost:8080/bartender

JSON to POST a new order:
{
    "customer": "28",
    "drink": "DRINK"
}

GET request to get de orders listed - JSON gruoped by customer:
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