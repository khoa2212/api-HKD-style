CREATE TABLE IF NOT EXISTS wishlists (
    id uuid DEFAULT gen_random_uuid(),
    user_id uuid NOT NULL,
    product_id uuid NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id),
    CONSTRAINT wishlist_product_fk FOREIGN KEY(product_id) REFERENCES products(id)
);