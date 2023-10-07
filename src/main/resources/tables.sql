CREATE TABLE user_accounts (
    id SERIAL PRIMARY KEY,
    login_name VARCHAR(100) UNIQUE NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password_hash VARCHAR(4000) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
)