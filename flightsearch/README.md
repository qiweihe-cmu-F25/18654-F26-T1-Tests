# Flight Search

## API Documentation

### Search Flights

```http
POST /api/v1/flightsearch/flights HTTP/1.1
Host: localhost:18085
Content-Type: application/json
```

#### Request Body

| Parameter          | Type   | Required | Description                                 |
|--------------------|--------|----------|---------------------------------------------|
| departure_airport  | string | Yes      | The departure airport, in [IATA code](https://www.iata.org/en/publications/directories/code-search/)                 |
| arrival_airport    | string | Yes      | The arrival airport, in [IATA code](https://www.iata.org/en/publications/directories/code-search/)                    |
| departure_datetime | string | No       | The departure date (YYYY-MM-DD format)|
| airline            | string | No       | The airline carrier, in [IATA code](https://www.iata.org/en/publications/directories/code-search/)                    |
| max_price          | number | No       | The maximum flight price                    |
| min_price          | number | No       | The minimum flight price                    |
| num_passengers     | number | No       | The number of passengers                    |
| flight_id          | string | No       | The specific flight ID to search for       |

#### Example Request

```http
POST /api/v1/flightsearch/flights HTTP/1.1
Host: localhost:18085
Content-Type: application/json

{
    "departure_airport": "JFK",
    "arrival_airport": "LAX",
    "departure_datetime": "2024-01-01",
    "max_price": 500,
    "num_passengers": 2
}
```

#### Example Response

```json
{
    "flights": [
        {
            "flight_id": "511b62a4-dd11-45a6-a0c7-f013cd8222b1",
            "departure_datetime": "2024-05-07T16:57:00Z",
            "airtime_minutes": 216,
            "flight_number": "329",
            "distance_miles": 1598,
            "total_seats": 500,
            "available_seats": 500,
            "aircraft": "Boeing 787 Dreamliner",
            "flight_price": 299.99,
            "origin": "JFK",
            "destination": "SJU",
            "carrier": "DL"
        },
        {
            "flight_id": "c572e8b6-9b36-469c-9ae5-76d3df1e8ce8",
            "departure_datetime": "2024-12-08T08:59:00Z",
            "airtime_minutes": 376,
            "flight_number": "422",
            "distance_miles": 2475,
            "total_seats": 151,
            "available_seats": 151,
            "aircraft": "Airbus A350 XWB",
            "flight_price": 388.99,
            "origin": "JFK",
            "destination": "LAX",
            "carrier": "DL"
        }
    ]
}
```
