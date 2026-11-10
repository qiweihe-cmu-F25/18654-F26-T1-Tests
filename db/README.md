

## All psql CREATE TABLE statements

These are ONLY for reference.

```roomsql

CREATE TABLE IF NOT EXISTS users (
	id uuid DEFAULT gen_random_uuid() NOT NULL PRIMARY KEY,
	username text NOT NULL,
	email text NOT NULL,
	password_hash text NOT NULL
);

CREATE TABLE IF NOT EXISTS trips (
	id uuid DEFAULT gen_random_uuid() NOT NULL PRIMARY KEY,
	name varchar(80),
	owner_id uuid REFERENCES users (id),
	start_date date,
	end_date date
);

CREATE TABLE IF NOT EXISTS expenses (
	id uuid DEFAULT gen_random_uuid() NOT NULL PRIMARY KEY,
	amount numeric(18,4),
	trip_id uuid REFERENCES trips (id)
);

CREATE TABLE IF NOT EXISTS user_trip_map
(
	user_id uuid REFERENCES users (id),
	trip_id uuid REFERENCES trips (id)
);


CREATE TABLE IF NOT EXISTS flight_bookings (
    id uuid DEFAULT gen_random_uuid() NOT NULL PRIMARY KEY,
    passenger_count integer NOT NULL,
    pnr_number varchar(10) NOT NULL,
    departure_iata_code char(3) NOT NULL,
    arrival_iata_code char(3) NOT NULL,
    booking_email_id varchar(30) NOT NULL,
    carrier_iata_code varchar(2) NOT NULL,
    timestamp timestamp NOT NULL,
    expense_id uuid REFERENCES expenses (id),
    trip_id uuid REFERENCES trips (id),
    user_id uuid REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS hotel_bookings (
    id uuid DEFAULT gen_random_uuid() NOT NULL PRIMARY KEY,
    hotel_id    varchar(80) NOT NULL,
    city        varchar(80) NOT NULL,
    country     varchar(80) NOT NULL,
    reservation_id  varchar(80) NOT NULL,
    primary_guest   varchar(80) NOT NULL,
    timestamp timestamp NOT NULL,
    expense_id uuid REFERENCES expenses (id),
    trip_id uuid REFERENCES trips (id),
    user_id uuid REFERENCES users (id)
);

```

