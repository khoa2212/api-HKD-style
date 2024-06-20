CREATE TABLE IF NOT EXISTS users (
    id uuid DEFAULT gen_random_uuid(),
    email VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(255),
    display_name VARCHAR(255),
    role VARCHAR(5) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS addresses (
    id uuid DEFAULT gen_random_uuid(),
    address VARCHAR(255),
    city VARCHAR(50),
    country VARCHAR(50),
    zip VARCHAR(50),
    phone_number VARCHAR(10),
    user_id uuid NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id),
    CONSTRAINT user_address_fk FOREIGN KEY(user_id)
    REFERENCES users(id)
);