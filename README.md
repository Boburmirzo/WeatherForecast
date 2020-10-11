# Forecast Weather REST API 

Implement a program that monitors weather forecasts for specific locations. 

This API provides basic consolidated average info retrieved from either openweathermap.org or mocky.io.

Note: If no provider parameter is provided, openweathermap.org will be used as default provider.

## Design considerations
- The API supports two levels of security by using Spring Security and JSON Web Token.

- Two levels of caching are provided, one at the client level (ETags) and the other at the application level (Redis to cache the static city information).

- Logging: slf4j was used to decouple from any specific implementation. The underlying logging is provided by LogBack. The generated log file is called weatherLogging.log,
located in ./logs directory.

- Exception handling: all the errors and exceptions are gracefully managed.

- Circuit Breaker Pattern: is used to manage in properly way the interaction with the API provider.

- Documentation: all the classes are documented (Javadocs) and also the API is exposed via Swagger.

- CityID: due the API provider recommends to use the API with the cityID, when the user provides only the city name and ISO country code, the application,
by using these parameters look for the cityId (Redis cached info), and then the provider API is invoked only by using the cityID.

- No hard coded values. All the config properties are defined in the `application.properties` file

- The providers are retrieved dynamically (at runtime) by using a factory that creates instances belonging to the SourceProvider hierarchy.


## Getting Started

1. Do mvn clean install
2. Run ForecastWeatherApplication Spring boot application

After this the Forecast Weather API will start on  http://localhost:8080.

### Prerequisites

A version of JDK 8 should be installed in order to run the application.


### Running the web services

1. To get access to the API, you should get a token in the following endpoint:

   http://localhost:8080/weather/generateToken

   Passing as parameter:
   {
    "parameter":"weatherSubject"
   }

   and using BASIC auth with the following credentials:

        username=user
        password=cloudator@2020


   After invoking these endpoint you will get something like this:

   {
       "code": "SUCCESS",
       "result": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdHJpbmciLCJleHAiOjE2MDI0MTM4Mzd9.7tSRbmvbRoQ2IY539pHqx__0nO9d4Bf-3fkga-862_c"
   }


   You should copy all the result value (this is the generated token) in order to invoke other endpoints.

2. After generating the token, you can invoke the statistics by invoking:

   `http://localhost:8080/weather/data`

    Passing as parameter, one of the following JSON payloads:


   Endpoint can receive as a parameter following:
   
         "id": 0 - Id of city if it is known - optional
         "isoCountryCode": "string" - Country iso code - optional
         "limitDay": 0 - limit number of days - optional by default 5 days
         "limitTemperature": 0 - limit temperature - mandatory
         "name": "string" - name of city - mandatory
   
   Request:
    {
      "parameter": {
        "limitTemperature": 10,
        "name": "London"
      }
    }
    
    and using BASIC auth with the following credentials:

       username=user
       password=cloudator@2020
   Response:
     {
       "code": "SUCCESS",
       "result": {
         "city": {
           "id": 2643743,
           "name": "London",
           "limitTemperature": 10
         },
         "dailyTemperatures": [
           {
             "unit": "celsius",
             "value": 13.47
           },
           {
             "unit": "celsius",
             "value": 12.25
           },
           {
             "unit": "celsius",
             "value": 10.88
           },
           {
             "unit": "celsius",
             "value": 10.58
           },
           {
             "unit": "celsius",
             "value": 13.25
           },
           {
             "unit": "celsius",
             "value": 13.15
           },
           {
             "unit": "celsius",
             "value": 11.39
           },
           {
             "unit": "celsius",
             "value": 11.55
           },
           {
             "unit": "celsius",
             "value": 11.28
           },
           {
             "unit": "celsius",
             "value": 10.35
           },
           {
             "unit": "celsius",
             "value": 10.23
           },
           {
             "unit": "celsius",
             "value": 11.22
           },
           {
             "unit": "celsius",
             "value": 10.99
           },
           {
             "unit": "celsius",
             "value": 10.48
           },
           {
             "unit": "celsius",
             "value": 10.05
           },
           {
             "unit": "celsius",
             "value": 11.37
           },
           {
             "unit": "celsius",
             "value": 14.17
           },
           {
             "unit": "celsius",
             "value": 14.09
           },
           {
             "unit": "celsius",
             "value": 11.81
           },
           {
             "unit": "celsius",
             "value": 11.42
           },
           {
             "unit": "celsius",
             "value": 11.3
           },
           {
             "unit": "celsius",
             "value": 10.47
           },
           {
             "unit": "celsius",
             "value": 10.33
           },
           {
             "unit": "celsius",
             "value": 10.99
           },
           {
             "unit": "celsius",
             "value": 12.41
           },
           {
             "unit": "celsius",
             "value": 12.1
           },
           {
             "unit": "celsius",
             "value": 10.89
           }
         ]
       }
     }

      And you should pass in the header, the previous generated token:

      `authorizationToken=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3ZWF0aGVyU3ViamVjdCIsImV4cCI6MTUzOTU2NzI0OX0.J3Xli1EV-T_cP-nQ_uJbkYGcYJdGINSvlmrwC6cSiHY`

    Note: the supported sourceProviderKey are `mocky.sourceProviderKey` and `openweathermap.sourceProviderKey`. If this param is not provided, openweathermap.org will be used as default provider.
    Also, for now Token based check is disabled for checking purposes.
## More info

You can run check all the functionalities exposed by the API in: `http://localhost:8080/swagger-ui.html`


