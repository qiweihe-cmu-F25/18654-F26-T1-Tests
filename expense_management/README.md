# Expense Management

## API Documentation

### Add Expense

#### Request Body
| Field                  | Type   | Required | Description                                        |
|------------------------|--------|----------|----------------------------------------------------|
| trip_id                | number | Yes      | Unique identifier for the trip                     |
| amount                 | number | Yes      | Total amount for the expense                        |
| description            | string | No       | Description of the expense                          |
| responsible_user_ids   | array[number]  | Yes      | List of user IDs responsible for the expense       |
| source                 | string | Yes      | Source of the expense (HOTEL, FLIGHT, CARHIRE, EXPERIENCE) |
| created_by_user_id     | number | Yes      | User ID of who created/paid for the expense        |
| occurred_on            | string | No       | Date when expense occurred (YYYY-MM-DD, defaults to today) |

#### Example Request

```http
POST /expenses HTTP/1.1
Host: localhost:18086
Content-Type: application/json

{
    "trip_id": 1,
    "amount": 150.75,
    "description": "Hotel",
    "responsible_user_ids": [1, 2],
    "source": "HOTEL",
    "created_by_user_id": 1,
    "occurred_on": "2025-01-15"
}
```

#### Example Response

```http
HTTP/1.1 200 OK
Content-Type: application/json

{
    "expense_id": 1
}
```

### Update Expense

#### Path Parameters

| Field                  | Type   | Required | Description                                        |
|------------------------|--------|----------|----------------------------------------------------|
| id                     | number | Yes      | Unique identifier for the expense                  |

#### Request Body

| Field                  | Type   | Required | Description                                        |
|------------------------|--------|----------|----------------------------------------------------|
| amount                 | number | Yes      | Total amount for the expense                        |

#### Example Request

```http
PUT /expenses/{id} HTTP/1.1
Host: localhost:18086
Content-Type: application/json

{
    "amount": 200.00
}
```

#### Example Response

```http
HTTP/1.1 200 OK
Content-Type: application/json

{
    "expense_id": 1
}
```

### Remove Expense

#### Path Parameters

| Field                  | Type   | Required | Description                                        |
|------------------------|--------|----------|----------------------------------------------------|
| id                     | number | Yes      | Unique identifier for the expense                  |

#### Example Request

```http
DELETE /expenses/{id} HTTP/1.1
Host: localhost:18086
```

#### Example Response

```http
HTTP/1.1 204 No Content
```

### Query Expenses

#### Request Parameters

| Field  | Type   | Required | Description                                        |
|--------|--------|----------|----------------------------------------------------|
| tripId | number | Yes      | Unique identifier for the trip                     |
| userId | number | Yes      | Unique identifier for the user                     |

#### Example Request

```http
GET /expenses?tripId=1&userId=1 HTTP/1.1
Host: localhost:18086
```

#### Example Response

```http
HTTP/1.1 200 OK
Content-Type: application/json

150.75
```

## Analytics Features

### Get User Summary

Retrieve period analytics for a single user in a trip, including how much they paid, owe, net balance, and per-category breakdown.

#### Request Parameters

| Field           | Type   | Required | Description                                        |
|-----------------|--------|----------|----------------------------------------------------|
| tripId          | number | Yes      | Unique identifier for the trip                     |
| userId          | number | Yes      | Unique identifier for the user                     |
| fromInclusive   | string | Yes      | Start date (YYYY-MM-DD, inclusive)               |
| toExclusive     | string | Yes      | End date (YYYY-MM-DD, exclusive)                 |

#### Example Request

```http
GET /expenses/summary?tripId=1&userId=1&fromInclusive=2025-01-01&toExclusive=2025-02-01 HTTP/1.1
Host: localhost:18086
```

#### Example Response

```http
HTTP/1.1 200 OK
Content-Type: application/json

{
    "paid_by_user": 200.00,
    "share_of_user": 150.00,
    "net": 50.00,
    "by_category": {
        "HOTEL": 100.00,
        "FLIGHT": 0.00,
        "CARHIRE": 0.00,
        "EXPERIENCE": 0.00
    }
}
```

#### Business Rules

- **Period Semantics**: Includes expenses where `occurred_on` is >= `fromInclusive` and < `toExclusive`
- **Equal Split**: Each expense is split equally among all `responsible_user_ids`
- **Calculations**:
  - `paid_by_user`: Sum of amounts where `created_by_user_id` == `userId`
  - `share_of_user`: Sum of user's shares for expenses where user is in `responsible_user_ids`
  - `net`: `paid_by_user` - `share_of_user`
  - `by_category`: User's shares grouped by `ExpenseSource`
- **Rounding**: All amounts are rounded to 2 decimal places using HALF_UP

### Get Balance Sheet

Compute who owes whom for the entire trip using equal split calculations across all expenses.

#### Request Parameters

| Field  | Type   | Required | Description                                        |
|--------|--------|----------|----------------------------------------------------|
| tripId | number | Yes      | Unique identifier for the trip                     |

#### Example Request

```http
GET /expenses/balance-sheet?tripId=1 HTTP/1.1
Host: localhost:18086
```

#### Example Response

```http
HTTP/1.1 200 OK
Content-Type: application/json

{
    "net_by_user": {
        "1": 75.50,
        "2": -25.25,
        "3": -50.25
    }
}
```

#### Business Rules

- **Participants**: Any user who appears as `created_by_user_id` or in any `responsible_user_ids`
- **Equal Split**: Each expense amount is divided equally among all users in `responsible_user_ids`
- **Net Calculation**: For each user: `(total paid) - (total share owed)`
- **Rounding**: All calculations use 10-decimal precision internally, final results rounded to 2 decimal places using HALF_UP
- **Balance Invariant**: Sum of all `net_by_user` values should equal 0.00 ± 0.01 (allowing 1-cent drift due to rounding)

## Expense Sources

The following expense sources are supported:

- **HOTEL**: Accommodation expenses
- **FLIGHT**: Flight and airfare costs  
- **CARHIRE**: Car rental expenses
- **EXPERIENCE**: Activities, tours, and experiences