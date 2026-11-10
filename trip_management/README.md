# Trip Management

## API Documentation

### Create Trip

#### Request Body
| Field      | Type   | Required | Description                                        |
|------------|--------|----------|----------------------------------------------------|
| trip_name  | string | Yes      | Name of the trip                     |
| start_date | date   | Yes      | Start date of the trip                        |
| end_date   | date   | Yes      | End date of the trip                          |
| members    | array[number]  | Yes      | List of user IDs that are part of the trip       |

#### Example Request

```http
POST /trips HTTP/1.1
Host: localhost:18087
Content-Type: application/json

{
    "trip_name": "Trip to Paris",
    "start_date": "2024-07-01",
    "end_date": "2024-07-05",
    "members": [1, 2, 3]
}
```

#### Example Response

```http
HTTP/1.1 200 OK
Content-Type: application/json

{
    "id": 2,
    "trip_name": "Trip to Paris",
    "start_date": "2024-07-01",
    "end_date": "2024-07-05",
    "creator_id": 0,
    "bookings": [],
    "members": [
        {
            "user_id": 1,
            "role": "MEMBER"
        },
        {
            "user_id": 0,
            "role": "CREATOR"
        },
        {
            "user_id": 3,
            "role": "MEMBER"
        },
        {
            "user_id": 2,
            "role": "MEMBER"
        }
    ]
}
```

### Add Booking to Trip

#### Path Parameters

| Field                  | Type   | Required | Description                                        |
|------------------------|--------|----------|----------------------------------------------------|
| tripId                 | number | Yes      | Unique identifier for the trip                  |

#### Request Body

| Field                  | Type   | Required | Description                                        |
|------------------------|--------|----------|----------------------------------------------------|
| bookingId                 | number | Yes      | Booking ID for the trip                        |
| source                 | string | Yes      | Source of the booking                        |

#### Example Request

```http
POST /trips/{tripId}/bookings HTTP/1.1
Host: localhost:18087
Content-Type: application/json

{
    "booking_id": 1,
    "source": "HOTEL"
}
```

#### Example Response

```http
HTTP/1.1 200 OK
Content-Type: application/json

{
    "id": 2,
    "trip_name": "Trip to Paris",
    "start_date": "2024-07-01",
    "end_date": "2024-07-05",
    "creator_id": 0,
    "bookings": [
        {
            "booking_id": 1,
            "source": "HOTEL"
        }
    ],
    "members": [
        {
            "user_id": 1,
            "role": "MEMBER"
        },
        {
            "user_id": 0,
            "role": "CREATOR"
        },
        {
            "user_id": 3,
            "role": "MEMBER"
        },
        {
            "user_id": 2,
            "role": "MEMBER"
        }
    ]
}
```

### Update Trip

#### Path Parameters

| Field                  | Type   | Required | Description                                        |
|------------------------|--------|----------|----------------------------------------------------|
| tripId                 | number | Yes      | Unique identifier for the trip                  |

#### Request Body

| Field      | Type   | Required | Description                                        |
|------------|--------|----------|----------------------------------------------------|
| trip_name  | string | Yes      | Name of the trip                     |
| start_date | date   | Yes      | Start date of the trip                        |
| end_date   | date   | Yes      | End date of the trip                          |
| members    | array[number]  | No       | Accepted for compatibility; membership is managed via `/trips/{tripId}/members` |

#### Example Request

```http
PUT /trips/{tripId} HTTP/1.1
Host: localhost:18087
Content-Type: application/json

{
    "trip_name": "Trip to Paris",
    "start_date": "2024-07-01",
    "end_date": "2024-07-05",
    "members": [1, 2, 3]
}   
```

#### Example Response

```http
HTTP/1.1 200 OK
Content-Type: application/json

{
    "id": 2,
    "trip_name": "Trip to Paris",
    "start_date": "2024-07-01",
    "end_date": "2024-07-05",
    "creator_id": 0,
    "bookings": [],
    "members": [
        {
            "user_id": 1,
            "role": "MEMBER"
        },
        {
            "user_id": 0,
            "role": "CREATOR"
        }
    ]
}
```

### Delete Trip

#### Path Parameters

| Field                  | Type   | Required | Description                                        |
|------------------------|--------|----------|----------------------------------------------------|
| tripId                 | number | Yes      | Unique identifier for the trip                  |    

#### Example Request

```http
DELETE /trips/{tripId} HTTP/1.1
Host: localhost:18087
Content-Type: application/json
```

#### Example Response

```http
HTTP/1.1 204 No Content
```

### List All Trips

#### Example Request

```http
GET /trips HTTP/1.1
Host: localhost:18087
Content-Type: application/json  
```

#### Example Response

```http
HTTP/1.1 200 OK
Content-Type: application/json

[
    {
        "id": 1,
        "trip_name": "Trip to Paris",
        "start_date": "2024-07-01",
        "end_date": "2024-07-05",
        "creator_id": 0,
        "bookings": [],
        "members": [1, 2, 3]
    }
]
```

### Query Trip by ID

#### Path Parameters

| Field                  | Type   | Required | Description                                        |
|------------------------|--------|----------|----------------------------------------------------|
| tripId                 | number | Yes      | Unique identifier for the trip                  |

#### Example Request

```http
GET /trips/{tripId} HTTP/1.1
Host: localhost:18087
Content-Type: application/json
```

#### Example Response

```http
HTTP/1.1 200 OK
Content-Type: application/json

{
    "id": 1,
    "trip_name": "Trip to Paris",
    "start_date": "2024-07-01",
    "end_date": "2024-07-05",
    "creator_id": 0,
    "bookings": [],
    "members": [1, 2, 3]
}
```

### Add Member to Trip

#### Path Parameters

| Field                  | Type   | Required | Description                                        |
|------------------------|--------|----------|----------------------------------------------------|
| tripId                 | number | Yes      | Unique identifier for the trip                  |

#### Request Body

| Field   | Type   | Required | Description                                        |
|---------|--------|----------|----------------------------------------------------|
| user_id | number | Yes      | Unique identifier for the user. The user must already exist in userprofile. |
| role    | string | Yes      | Role of the user in the trip. Must be `MEMBER`; `CREATOR` is rejected with 400 because a trip has only one creator (the user who created it) |

#### Example Request

```http
POST /trips/{tripId}/members HTTP/1.1
Host: localhost:18087
Content-Type: application/json

{
    "user_id": 4,
    "role": "MEMBER"
}   
```

#### Example Response

```http
HTTP/1.1 200 OK
Content-Type: application/json

{
    "id": 2,
    "trip_name": "Trip to Paris",
    "start_date": "2024-07-01",
    "end_date": "2024-07-05",
    "creator_id": 0,
    "bookings": [],
    "members": [1, 2, 3, 4]
}
```

### Remove Member from Trip

#### Path Parameters

| Field                  | Type   | Required | Description                                        |
|------------------------|--------|----------|----------------------------------------------------|
| tripId                 | number | Yes      | Unique identifier for the trip                  |    
| userId                 | number | Yes      | Unique identifier for the user                  |

#### Example Request

```http
DELETE /trips/{tripId}/members/{userId} HTTP/1.1
Host: localhost:18087
Content-Type: application/json  
```

#### Example Response

```http
HTTP/1.1 200 OK
Content-Type: application/json

{
    "id": 2,
    "trip_name": "Trip to Paris",
    "start_date": "2024-07-01",
    "end_date": "2024-07-05",
    "creator_id": 0,
    "bookings": [],
    "members": [1, 2, 3]
}
```

### Check if User is in Trip

#### Path Parameters

| Field                  | Type   | Required | Description                                        |
|------------------------|--------|----------|----------------------------------------------------|
| tripId                 | number | Yes      | Unique identifier for the trip                  |    
| userId                 | number | Yes      | Unique identifier for the user                  |

#### Example Request

```http 
GET /trips/{tripId}/members/{userId} HTTP/1.1
Host: localhost:18087
Content-Type: application/json
```

#### Example Response

```http 
HTTP/1.1 200 OK
Content-Type: application/json

true
```













