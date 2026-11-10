# Experience Search

## API Documentation

### Search Experiences

```http
POST /experience-search/search HTTP/1.1
Host: localhost:18102
Content-Type: application/json
```

#### Request Body

| Field                    | Type   | Required | Description                                        |
|--------------------------|--------|----------|----------------------------------------------------|
| date                     | string | No      | The date for the experience search (YYYY-MM-DD)   |
| max_price                | number | No       | The maximum price for the experience                |
| min_price                | number | No       | The minimum price for the experience                |
| organizer                | string | No       | The organizer of the experience                     |
| name                     | string | No       | The name of the experience                          |
| latitude                 | number | No       | The latitude for location-based search              |
| longitude                | number | No       | The longitude for location-based search             |
| number_of_participants   | number | No       | The number of participants for the experience       |

#### Example Request

```json
{
  "date": "2024-01-01",
  "organizer": "brooklyn",
  "max_price": 100
}
```

#### Example Response

```json
{
    "experiences": [
        {
            "id": "72b20d73-1e66-46f0-8b10-ec8b945381ff",
            "organizer": "One Brooklyn Fund/Brooklyn Arts Council",
            "event_name": "Destination: Brooklyn",
            "address": "20 Jay St #616",
            "city": "Brooklyn",
            "state": "New York",
            "postcode": 11201,
            "borough": "BROOKLYN",
            "latitude": "40.704102",
            "longitude": "-73.987068",
            "nta": "DUMBO-Vinegar Hill-Downtown Brooklyn-Boerum Hill",
            "capacity": 2947,
            "price_per_person": "67"
        },
        // ... additional experience objects ...
    ]
}
```

---
