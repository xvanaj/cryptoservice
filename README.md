# Crypto investment app exercise
- Created as part of the interview process for unnamed company

## Requirements
- Reads all the prices from the csv files
- Calculates oldest/newest/min/max for each crypto for the whole month
- Exposes an endpoint that will return a descending sorted list of all the cryptos,
comparing the normalized range (i.e. (max-min)/min)
- Exposes an endpoint that will return the oldest/newest/min/max values for a requested
crypto
- Exposes an endpoint that will return the crypto with the highest normalized range for a
specific day
Things to consider:
- Documentation is our best friend, so it will be good to share one for the endpoints
- Initially the cryptos are only five, but what if we want to include more? Will the
recommendation service be able to scale?
- New cryptos pop up every day, so we might need to safeguard recommendations service
endpoints from not currently supported cryptos
- For some cryptos it might be safe to invest, by just checking only one month's time
frame. However, for some of them it might be more accurate to check six months or even
a year. Will the recommendation service be able to handle this?

### Api documentation
- by default visible on http://localhost:8080/swagger-ui/index.html
- link to swagger can be adjusted in application.properties file
- Exposed only one endpoint to retrieve cryptocurrencies. This endpoint has optional parameters allowing us to perform all requested operations from requirements. If required, new endpoint can be created and could use existing functionality. We would just have to create SearchRequest manually in code and pass it to search method. 

### Tech debt
- GET endpoint for cryptocurrency has 5 parameters. Should be wrapped in SearchRequest and method 
should have only one parameter on the input. 
- GET endpoint could be extended to search using POST with body containing filters and sorting/pagination. 
Filtering could be done on more fields and could use various operations like EQUAL, LIKE, BETWEEN (for dates) etc   
- move to cloud - AWS lambda is easily scalable and you can use rate limiting out of the box