# Car Hire Tracking

## API Documentation

```http
POST /carhire-tracking/ HTTP/1.1
Host: localhost:18101
Content-Type: application/json
```

| Field                  | Type   | Required | Description                                        |
|------------------------|--------|----------|----------------------------------------------------|
| data                   | object | Yes      | Contains car hire and booking details               |
| data.carhire           | object | Yes      | Information about the car hire                      |
| data.carhire.operator  | string | No      | The name of the car hire operator                   |
| data.carhire.license_plate | string | No  | The license plate of the vehicle                   |
| data.carhire.vin      | string | No      | The Vehicle Identification Number (VIN)            |
| data.carhire.vehicle_year | number | No  | The year the vehicle was manufactured               |
| data.carhire.phone     | string | No      | Contact phone number for the operator               |
| data.carhire.website   | string | No       | Website of the car hire operator                    |
| data.carhire.id        | string | No      | Unique identifier for the car hire                  |
| data.carhire.daily_rate | string | No     | Daily rental rate for the vehicle                   |
| data.carhire.vehicle_type | string | No   | Type of the vehicle (e.g., Sedan, SUV)             |
| data.carhire.seat_capacity | number | No | Number of seats in the vehicle                      |
| data.booking           | object | Yes      | Booking details for the car hire                    |
| data.booking.carhire_id | string | Yes   | ID of the car hire being booked                     |
| data.booking.id        | string | Yes      | Unique identifier for the booking                   |
| data.booking.start_date | string | Yes   | Start date of the booking (ISO 8601 format)       |
| data.booking.end_date  | string | Yes   | End date of the booking (ISO 8601 format)         |
| amount_paid            | number | No      | Total amount paid for the booking                   |
| trip_id                | number | No*      | Unique identifier for the trip                      |
| responsible_user_ids   | array  | No*      | List of user IDs responsible for the booking       |

*Note: `trip_id` and `responsible_user_ids` are required if `amount_paid` is provided. Omitting required booking fields typically results in `400 Bad Request`.

#### Example Request

```json
{
    "data": {
        "carhire": {
            "operator": "TAHIR,MASHHOOD",
            "license_plate": "T698633C",
            "vin": "4T1C31AK2LU540792",
            "vehicle_year": 2020,
            "phone": "(646)780-0129",
            "website": null,
            "id": "c6a28fde-94c1-43f3-a284-bf3d40870b0a",
            "daily_rate": "156",
            "vehicle_type": "Sedan",
            "seat_capacity": 4
        },
        "booking": {
            "carhire_id": "c6a28fde-94c1-43f3-a284-bf3d40870b0a",
            "id": "b44aa1e1-96d4-41f5-bced-40ff6a2f464c",
            "start_date": "2024-01-04T00:00:00.000Z",
            "end_date": "2024-01-07T00:00:00.000Z"
        }
    },
    "amount_paid": 400,
    "trip_id": 1,
    "responsible_user_ids": [1,2]
}
```

#### Example Response

```text
Car hire booking received
```
