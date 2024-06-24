CREATE TABLE IF NOT EXISTS products (
    id uuid DEFAULT gen_random_uuid(),
    name VARCHAR(255),
    category VARCHAR(255),
    status VARCHAR(255),
    stock INT DEFAULT 1,
    sales INT DEFAULT 0,
    description TEXT,
    price DECIMAL DEFAULT 0,
    attachment TEXT,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS carts (
    id uuid DEFAULT gen_random_uuid(),
    user_id uuid NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS cart_items (
    id uuid DEFAULT gen_random_uuid(),
    cart_id uuid NOT NULL,
    product_id uuid NOT NULL,
    quantity INT DEFAULT 0,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id),
    CONSTRAINT cart_cart_item_fk FOREIGN KEY(cart_id) REFERENCES carts(id),
    CONSTRAINT product_cart_item_fk FOREIGN KEY(product_id) REFERENCES products(id)
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
    id uuid DEFAULT gen_random_uuid(),
    order_id uuid NOT NULL,
    product_id uuid NOT NULL,
    quantity INT NOT NULL CHECK(quantity > 0),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id),
    CONSTRAINT order_order_item_fk FOREIGN KEY(order_id) REFERENCES orders(id),
    CONSTRAINT product_order_item_fk FOREIGN KEY(product_id) REFERENCES products(id)
);

CREATE TABLE IF NOT EXISTS reviews (
    id uuid DEFAULT gen_random_uuid(),
    user_id uuid NOT NULL,
    product_id uuid NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    rating INT,
    content TEXT,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id),
    CONSTRAINT product_review_fk FOREIGN KEY(product_id) REFERENCES products(id)
);

