# Hotel Tracking

## API Documentation

```http
POST /hotel-tracking/ HTTP/1.1
Host: localhost:18083
Content-Type: application/json
```

| Field                    | Type    | Required | Description                                        |
|--------------------------|---------|----------|----------------------------------------------------|
| data                     | object  | Yes      | Hotel booking details                              |
| data.hotel               | object  | Yes      | Hotel information                                   |
| data.hotel.hotel_id      | string  | Yes      | Unique identifier for the hotel                    |
| data.hotel.borough_code  | number  | No      | Borough code                                       |
| data.hotel.block         | number  | No       | Block number                                       |
| data.hotel.lot           | number  | No       | Lot number                                         |
| data.hotel.street_number  | string  | No      | Street number                                      |
| data.hotel.street_name    | string  | No      | Street name                                        |
| data.hotel.zipcode       | number  | No      | Zip code                                           |
| data.hotel.building_class | string  | No       | Building class                                     |
| data.hotel.owner_name    | string  | No       | Owner's name                                       |
| data.hotel.borough       | string  | No       | Borough name                                       |
| data.hotel.latitude      | string  | No       | Latitude of the hotel                              |
| data.hotel.longitude     | string  | No       | Longitude of the hotel                             |
| data.hotel.area          | string  | No       | Area of the hotel                                  |
| data.booking             | object  | Yes      | Booking information                                 |
| data.booking.id          | string  | Yes      | Unique identifier for the booking                  |
| data.booking.start_date  | string  | Yes      | Start date of the booking (ISO 8601 format)       |
| data.booking.end_date    | string  | Yes      | End date of the booking (ISO 8601 format)         |
| data.booking.one_person_rooms | number  | No   | Number of one-person rooms                          |
| data.booking.two_person_rooms | number  | No   | Number of two-person rooms                          |
| data.booking.four_person_rooms | number  | No   | Number of four-person rooms                         |
| amount_paid              | number  | No       | Total amount paid for the booking                  |
| trip_id                  | number  | No*      | Unique identifier for the trip                     |
| responsible_user_ids     | array   | No*      | List of user IDs responsible for the booking       |

*Note: `trip_id` and `responsible_user_ids` are required if `amount_paid` is provided. Omitting required booking/`hotel_id` fields returns `400 Bad Request`.

#### Example Request

```json
{
    "data": {
        "hotel": {
            "hotel_id": "60ba1175-f22b-48d0-b576-edd1505d7ae6",
            "borough_code": 1,
            "block": 8,
            "lot": 39,
            "street_number": "32",
            "street_name": "PEARL STREET",
            "zipcode": 10004,
            "building_class": "H3",
            "owner_name": "32 PEARL, LLC",
            "borough": "MANHATTAN",
            "latitude": "40.703235",
            "longitude": "-74.012421",
            "area": "Battery Park City-Lower Manhattan"
        },
        "booking": {
            "hotel_id": "60ba1175-f22b-48d0-b576-edd1505d7ae6",
            "id": "c641aebe-7392-409e-9d26-d79a18f97d20",
            "start_date": "2024-01-01T00:00:00.000Z",
            "end_date": "2024-01-03T00:00:00.000Z",
            "one_person_rooms": 1,
            "two_person_rooms": 1,
            "four_person_rooms": 1
        }
    },
    "amount_paid": 400,
    "trip_id": 1,
    "responsible_user_ids": [1, 2]
}
```

#### Example Response

```text
Hotel booking received
```