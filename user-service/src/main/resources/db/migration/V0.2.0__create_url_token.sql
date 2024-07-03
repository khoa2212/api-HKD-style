CREATE TABLE IF NOT EXISTS url_tokens (
    id uuid DEFAULT gen_random_uuid(),
    user_id uuid NOT NULL,
    token_value VARCHAR(255) NOT NULL,
    token_type VARCHAR(30) NOT NULL,
    expire_at TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id),
    CONSTRAINT url_tokens_user_fk FOREIGN KEY(user_id) REFERENCES users(id)
);