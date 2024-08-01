CREATE TABLE IF NOT EXISTS users (
    user_id BINARY(16) PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255)
    );