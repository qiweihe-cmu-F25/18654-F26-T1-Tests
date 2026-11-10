# Car Hire Search

## API Documentation

### Search Car Hires

```http
POST /carhire-search/search HTTP/1.1
Host: localhost:18100
Content-Type: application/json
```

#### Request Body

| Field            | Type   | Required | Description                                        |
|------------------|--------|----------|----------------------------------------------------|
| start_date       | string | No      | The start date for the car hire search (YYYY-MM-DD) |
| end_date         | string | No      | The end date for the car hire search (YYYY-MM-DD). Must not be before `start_date`, otherwise 400 |
| max_price        | number | No       | The maximum price for the car hire                  |
| min_price        | number | No       | The minimum price for the car hire                  |
| type             | string | No       | The type of car (SUV, Luxury SUV, Sedan, Limousine, Executive Sedan, Van, Luxury Sedan, Van) |
| num_passengers   | number | No       | The number of passengers                             |

#### Example Request

```json
{
    "start_date": "2024-01-01",
    "end_date": "2024-01-04",
    "max_price": 1000,
    "min_price": 200,
    "type": "Sedan",
    "num_passengers": 4
}
```

#### Example Response

```json
{
    "carhires": [
        {
            "operator": "ISLAM,MOHAMMAD,N",
            "license_plate": "T782158C",
            "vin": "1FMCU5K3XCKA90476",
            "vehicle_year": 2012,
            "phone": "(646)780-0129",
            "website": null,
            "id": "000048e5-f3aa-49f0-a498-18d6d55d12da",
            "daily_rate": "162",
            "vehicle_type": "Van",
            "seat_capacity": 9
        },
        {
            "operator": "KRUJA,CORP",
            "license_plate": "T102276C",
            "vin": "2T3P1RFVXMC233468",
            "vehicle_year": 2021,
            "phone": "(646)780-0129",
            "website": null,
            "id": "0001e435-78bc-4532-8dd6-4a5c58f67ab8",
            "daily_rate": "408",
            "vehicle_type": "Limousine",
            "seat_capacity": 7
        }
    ]
}
```

---
