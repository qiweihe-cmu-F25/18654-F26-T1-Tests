# Flight Tracking

## API Documentation

```http
POST /flight-tracking/ HTTP/1.1
Host: localhost:18082
Content-Type: application/json
```

| Field                    | Type    | Required | Description                                        |
|--------------------------|---------|----------|----------------------------------------------------|
| data                     | object  | Yes      | Flight details                                     |
| data.flight_id           | string  | Yes      | Unique identifier for the flight                   |
| data.carrier             | string  | Yes      | Carrier of the flight                              |
| data.flight_number       | string  | Yes      | Flight number                                      |
| data.origin              | string  | Yes      | Origin airport code                                |
| data.destination         | string  | Yes      | Destination airport code                           |
| data.distance_miles      | number  | Yes      | Distance of the flight in miles                   |
| data.total_seats         | number  | Yes      | Total number of seats available                    |
| data.available_seats     | number  | Yes      | Number of available seats                           |
| data.aircraft            | string  | Yes      | Type of aircraft                                   |
| data.airtime_minutes     | number  | Yes      | Total airtime in minutes                           |
| data.departure_datetime  | string  | Yes      | Departure date and time (ISO 8601 format)        |
| data.flight_price        | number  | Yes      | Price of the flight                                |
| amount_paid              | number  | No       | Total amount paid for the booking                  |
| trip_id                  | number  | No*      | Unique identifier for the trip                     |
| responsible_user_ids     | array   | No*      | List of user IDs responsible for the booking       |

*Note: `trip_id` and `responsible_user_ids` are required if `amount_paid` is provided. Omitting other required `data` fields returns `400 Bad Request`.

#### Example Request

```json
{
    "data": {
        "flight_id": "b7382241-39a4-4c11-b9b6-a756d029014f",
        "carrier": "VX",
        "flight_number": "407",
        "origin": "JFK",
        "destination": "LAX",
        "distance_miles": 2475,
        "total_seats": 151,
        "available_seats": 149,
        "aircraft": "Airbus A320",
        "airtime_minutes": 313,
        "departure_datetime": "2024-06-30T09:40:00.000Z",
        "flight_price": "284.99"
    },
    "amount_paid": 400,
    "trip_id": 1,
    "responsible_user_ids": [1, 2]
}
```

#### Example Response

```text
Flight booking received
```