CREATE TABLE user_accounts (
    id SERIAL PRIMARY KEY,
    login_name VARCHAR(100) UNIQUE NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password_hash VARCHAR(4000) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TYPE TRANSACTION_TYPE AS ENUM ('DEBIT', 'CREDIT');

CREATE TABLE transactions (
    id SERIAL,
    user_account_id INT REFERENCES user_accounts(id) NOT NULL,
    transaction_type TRANSACTION_TYPE NOT NULL,
    amount DECIMAL(12, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);