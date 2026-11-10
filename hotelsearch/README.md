# hotelsearch

## API Docs

## Search hotels

```http
POST /api/v1/hotelsearch/hotels HTTP/1.1
Host: localhost:18084
Content-Type: application/json
```

### Request Body

| Attribute         | Type                         | Optional | Description                              |
|-------------------|------------------------------|----------|-------------------------------------------|
| start_date        | Date                         | Yes      | The start date of the hotel stay.         |
| end_date          | Date                         | Yes      | The end date of the hotel stay.           |
| requested_rooms   | Map<RoomType, Integer>       | Yes      | The requested number of rooms by type. A map from `roomType` to number of rooms you wish to book of that type. `roomType` may be any combination of `one_person_rooms`, `two_person_rooms`, and `four_person_rooms`.     |
| price_range       | Map<RoomType, PriceRange>    | Yes      | The price range for each room type. A map from `roomType` to number of rooms you wish to the price range you wish to pay for that room. `roomType` may be any combination of `one_person_rooms`, `two_person_rooms`, and `four_person_rooms`.          |
| price_range.one_person_rooms.min_price  | number  | Yes      | Minimum price for one person rooms.                         |
| price_range.one_person_rooms.max_price  | number  | Yes      | Maximum price for one person rooms.                         |
| price_range.two_person_rooms.min_price  | number  | Yes      | Minimum price for two person rooms.                         |
| price_range.two_person_rooms.max_price  | number  | Yes      | Maximum price for two person rooms.                         |
| price_range.four_person_rooms.min_price | number  | Yes      | Minimum price for four person rooms.                        |
| price_range.four_person_rooms.max_price | number  | Yes      | Maximum price for four person rooms.                        |
| hotel_id          | String                       | Yes      | The identifier for the hotel.             |
| name              | String                       | Yes      | The name of the hotel.                    |
| longitude         | Double                       | Yes      | The longitude of the hotel's location.    |
| latitude          | Double                       | Yes      | The latitude of the hotel's location.     |
| radius            | Double                       | Yes      | The search radius around the location. Must be >= 0; negative values return an empty hotel list. |


### Example Request

```http
POST /api/v1/hotelsearch/hotels HTTP/1.1
Host: localhost:18084
Content-Type: application/json

{
    "start_date": "2024-01-01",
    "end_date": "2024-01-03",
    "requested_rooms": {
        "one_person_rooms": 1,
        "two_person_rooms": 0,
        "four_person_rooms": 0
    },
    "price_range": {
        "one_person_rooms": {
            "min_price": 0,
            "max_price": 180
        }
    },
    "name": "marriott",
    "latitude": 40,
    "longitude": -73,
    "radius": 10
}
```
### Example Response

```json
{
    "hotels": [
        {
            "hotel_id": "ae85b885-0e00-420f-9209-3fe433a3c221",
            "borough_code": "1",
            "block": "839",
            "lot": "1201",
            "street_number": "33",
            "street_name": "WEST 37TH STREET",
            "zipcode": 10018,
            "building_class": "RH",
            "owner_name": "MARRIOTT OWNERSHIP RESORTS, INC.",
            "borough": "1",
            "room_rates": {
                "ONE_PERSON_RATE": 100,
                "TWO_PERSON_RATE": 130,
                "FOUR_PERSON_RATE": 240
            },
            "latitude": 40.750864,
            "longitude": -73.98439,
            "area": "Midtown-Midtown South",
            "address": "Lot 1201, Block 839\n33, WEST 37TH STREET\n10018"
        },
        {
            "hotel_id": "1393a4f1-e2a1-4259-ae2b-a2ebcca99f7f",
            "borough_code": "1",
            "block": "839",
            "lot": "1202",
            "street_number": "33",
            "street_name": "WEST 37TH STREET",
            "zipcode": 10018,
            "building_class": "RH",
            "owner_name": "MARRIOTT OWNERSHIP RESORTS, INC.",
            "borough": "1",
            "room_rates": {
                "ONE_PERSON_RATE": 120,
                "TWO_PERSON_RATE": 150,
                "FOUR_PERSON_RATE": 210
            },
            "latitude": 40.750864,
            "longitude": -73.98439,
            "area": "Midtown-Midtown South",
            "address": "Lot 1202, Block 839\n33, WEST 37TH STREET\n10018"
        }
    ]
}
```
