CREATE TABLE IF NOT EXISTS payment_details (
    id uuid DEFAULT gen_random_uuid(),
    amount bigint NOT NULL CHECK(amount > 0) DEFAULT 0,
    bank_code VARCHAR(5) NOT NULL,
    payment_info VARCHAR(255),
    payment_method VARCHAR(10),
    payment_date TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id)
);
