CREATE TABLE IF NOT EXISTS users (
    id uuid DEFAULT gen_random_uuid(),
    email VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(255),
    full_name VARCHAR(255),
    role VARCHAR(5) NOT NULL DEFAULT 'USER',
    address VARCHAR(255),
    city VARCHAR(50),
    phone_number VARCHAR(10),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id)
);