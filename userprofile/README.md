# userprofile

## API Docs

### Create New User

```http
POST /api/v1/userprofile HTTP/1.1
Host: localhost:18081
Content-Type: application/json
```

#### Request Body

| Variable Name | Type   | Optional | Description                     |
|---------------|--------|----------|---------------------------------|
| username      | String | No       | The username of the user.        |
| email         | String | No       | The email address of the user.   |
| password      | String | No       | The password for the user.       |

#### Example Request

```http
POST /api/v1/userprofile HTTP/1.1
Host: localhost:18081
Content-Type: application/json
Content-Length: 104

{
    "username": "jane3",
    "password": "myamazingpassword",
    "email": "jane@mail.com"
}
```

#### Example Response

```http
HTTP/1.1 200 OK
Content-Type: application/json

{
    "message": "Success",
    "userId": 1
}
```

### Get User

```http
GET /api/v1/userprofile/<userId> HTTP/1.1
Host: localhost:18081
```

| Attribute        | Type                | Optional | Description |
|------------------|---------------------| -------- | ----------- |
| userId           | Long                | No       | The ID of the user. |

#### Example Request

```http
GET /api/v1/userprofile/2 HTTP/1.1
Host: localhost:18081
```
### Login

```http
POST /api/v1/userprofile/login HTTP/1.1
Host: localhost:18081
Content-Type: application/json
```

#### Request Body

| Variable Name | Type   | Optional | Description                     |
|---------------|--------|----------|---------------------------------|
| username      | String | No       | The username of the user.        |
| password      | String | No       | The password for the user.       |

#### Example Request

```http
POST /api/v1/userprofile/login HTTP/1.1
Host: localhost:18081
Content-Type: application/json
Content-Length: 40

{
    "username": "jane3",
    "password": "myamazingpassword"
}
```

#### Example Response

```http
HTTP/1.1 200 OK
Content-Type: application/json

{
    "id": 1,
    "username": "test",
    "jwt": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiZXhwIjoxNzI3Mzg3ODE5LCJpYXQiOjE3MjczODQyMTksInVzZXJuYW1lIjoidGVzdCJ9.TYmVxOcFCBRihHFz0QtK0D_h29N9MoXvg_cqKt4bdlc"
}
```