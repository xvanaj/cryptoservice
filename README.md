# Crypto investment app exercise
- Created as part of the interview process
## Requirements
- 48 hours to complete
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

## Solution
- Project contains docker file. Application can be started with following commands:

`docker build -t cryptoapp .`

`docker run -p 8080:8080 cryptoapp`
- Swagger is then accessible on http://localhost:8080/swagger-ui/index.html
- All requests are rate limited by ip with bucket4j library
- Since reading of csv files is expensive operation I added cache for better performance. Cache is cleared automatically every minute or can be cleared manually by invoking /clear-cache endpoint  
- Project has 96% test coverage and no sonar issues

### Api documentation
- Api is well-described via swagger as mentioned above http://localhost:8080/swagger-ui/index.html
- Exposed only one endpoint to retrieve cryptocurrencies. This endpoint has optional parameters allowing us to perform all operations from requirements
- Endpoint operations are covered by integration tests
- Swagger example:![Swagger example](/src/main/resources/static/swagger-example.png)

### Possible improvements 
- Move to cloud - AWS lambda is fast, easily scalable, and you can use rate limiting out of the box. Other option would be to use some api gateway
- Docker image uses fat JAR. Docker file could be updated to use multi-stage builds optimization of image size. Other option would be to generate docker file with gradle and jib    
- GET endpoint for cryptocurrency has 5 parameters. Maybe should be wrapped in SearchRequest so that we will have only one param on input. Problem is that generated swagger documentation is then less user-friendly and would need some tweaking. Therefore, I left params as they are for purposes of this exercise.  
- GET endpoint could be extended to search using POST with body containing filters and sorting/pagination.
  Filtering could be done on more fields and could use various operations like EQUAL, LIKE, BETWEEN (for dates) etc