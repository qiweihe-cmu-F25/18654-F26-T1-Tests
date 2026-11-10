# Experience Tracking

## API Documentation

```http
POST /experience-tracking/ HTTP/1.1
Host: localhost:18103
Content-Type: application/json
```

| Field                    | Type    | Required | Description                                        |
|--------------------------|---------|----------|----------------------------------------------------|
| experience               | object  | Yes      | Details of the experience                          |
| experience.id            | string  | Yes      | Unique identifier for the experience                |
| experience.organizer     | string  | Yes      | Name of the organizer                              |
| experience.event_name    | string  | Yes      | Name of the event                                  |
| experience.address       | string  | Yes      | Address of the event                               |
| experience.city          | string  | Yes      | City where the event is held                       |
| experience.state         | string  | Yes      | State where the event is held                      |
| experience.postcode      | number  | Yes      | Postcode of the event location                     |
| experience.borough       | string  | Yes      | Borough of the event location                      |
| experience.latitude       | number  | Yes      | Latitude of the event location                     |
| experience.longitude      | number  | Yes      | Longitude of the event location                    |
| experience.nta           | string  | Yes      | Neighborhood Tabulation Area (NTA)                |
| experience.capacity      | number  | Yes      | Capacity of the event                              |
| experience.price_per_person | number | Yes    | Price per person for the event                     |
| booking                  | object  | Yes      | Booking details for the experience                 |
| booking.experience_id    | string  | Yes      | ID of the experience being booked                  |
| booking.id               | string  | Yes      | Unique identifier for the booking                  |
| booking.start_date       | string  | Yes      | Start date of the booking (ISO 8601 format)       |
| booking.participants      | number  | Yes      | Number of participants for the booking             |
| amount_paid              | number  | No       | Total amount paid for the booking                  |
| trip_id                  | number  | No*      | Unique identifier for the trip                     |
| responsible_user_ids     | array   | No*      | List of user IDs responsible for the booking       |

*Note: `trip_id` and `responsible_user_ids` are required if amount_paid is provided.

#### Example Request

```json
{
    "experience": {
        "id": "2204992f-e520-43ba-9210-d7f84c8d87cd",
        "organizer": "Gotham Theater Company",
        "event_name": "Gotham Theater Company presents: Literary Event",
        "address": "545 Market St",
        "city": "Brooklyn",
        "state": "New York",
        "postcode": 11207,
        "borough": "BROOKLYN",
        "latitude": "40.646881",
        "longitude": "-74.039825",
        "nta": "DUMBO-Vinegar Hill-Downtown Brooklyn-Boerum Hill",
        "capacity": 7593,
        "price_per_person": "40"
    },
    "booking": {
        "experience_id": "2204992f-e520-43ba-9210-d7f84c8d87cd",
        "id": "677d9b81-c311-4aff-8915-3f2447fe93ef",
        "start_date": "2024-01-03T00:00:00.000Z",
        "participants": 5
    },
    "amount_paid": 400,
    "trip_id": 1,
    "responsible_user_ids": [1, 2]
}
```

#### Example Response

```text
Experience booking received
```