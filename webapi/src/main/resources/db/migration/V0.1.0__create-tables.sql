CREATE TABLE IF NOT EXISTS carts (
    id uuid DEFAULT gen_random_uuid(),
    user_id uuid NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS cart_items (
    cart_id uuid NOT NULL,
    product_id uuid NOT NULL,
    quantity INT DEFAULT 0,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(cart_id, product_id),
    CONSTRAINT cart_fk FOREIGN KEY(cart_id) REFERENCES carts(id)
);

CREATE TABLE IF NOT EXISTS orders (
    id uuid DEFAULT gen_random_uuid(),
    user_id uuid NOT NULL,
    total_price INT NOT NULL CHECK(total_price >= 0),
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(10),
    address VARCHAR(255),
    city VARCHAR(50),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS order_items (
    order_id uuid NOT NULL,
    product_id uuid NOT NULL,
    quantity INT NOT NULL CHECK(quantity > 0),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(order_id, product_id),
    CONSTRAINT order_fk FOREIGN KEY(order_id) REFERENCES orders(id)
);

CREATE TABLE IF NOT EXISTS reviews (
    id uuid DEFAULT gen_random_uuid(),
    user_id uuid NOT NULL,
    product_id uuid NOT NULL,
    rating INT,
    content TEXT,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id)
);

