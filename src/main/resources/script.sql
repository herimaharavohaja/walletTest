DROP DATABASE IF EXISTS wallet;

SELECT 'CREATE DATABASE wallet'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'wallet')
\gexec

\c wallet

DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'account_type') THEN
            CREATE TYPE "account_type" AS ENUM ('BANK', 'CASH', 'MOBILE_MONEY');
        END IF;
    END
$$;

CREATE TABLE IF NOT EXISTS currency
(
    currency_id   SERIAL PRIMARY KEY,
    currency_name VARCHAR(50) NOT NULL,
    currency_code VARCHAR(5)  NOT NULL
);

CREATE TABLE IF NOT EXISTS account
(
    account_id   SERIAL PRIMARY KEY,
    account_name VARCHAR(255) NOT NULL,
    currency_id  INT,
    account_type account_type NOT NULL DEFAULT 'CASH',
    FOREIGN KEY (currency_id) REFERENCES currency (currency_id)
);

DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'transaction_type') THEN
            CREATE TYPE "transaction_type" AS ENUM ('CREDIT', 'DEBIT');
        END IF;
    END
$$;

CREATE TABLE IF NOT EXISTS "transaction"
(
    transaction_id        SERIAL PRIMARY KEY,
    label                 VARCHAR(255)                        NOT NULL,
    amount                DOUBLE PRECISION                    NOT NULL,
    transaction_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    account_id            INT                                 NOT NULL,
    transaction_type      "transaction_type"                  NOT NULL,
    FOREIGN KEY (account_id) REFERENCES account (account_id)
);

CREATE TABLE IF NOT EXISTS transfer
(
    transfer_id           SERIAL PRIMARY KEY,
    debit_transaction_id  INT                                 NOT NULL,
    credit_transaction_id INT                                 NOT NULL,
    transfer_date         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (debit_transaction_id) REFERENCES "transaction" (transaction_id),
    FOREIGN KEY (credit_transaction_id) REFERENCES "transaction" (transaction_id)
);

CREATE TABLE IF NOT EXISTS currency_value
(
    currency_value_id       SERIAL PRIMARY KEY,
    source_currency_id      INT NOT NULL,
    destination_currency_id INT NOT NULL,
    value                   DOUBLE PRECISION NOT NULL,
    effective_date          DATE NOT NULL ,
    FOREIGN KEY (source_currency_id) REFERENCES currency (currency_id),
    FOREIGN KEY (destination_currency_id) REFERENCES currency (currency_id)
);

CREATE TABLE IF NOT EXISTS balance
(
    balance_id        SERIAL PRIMARY KEY,
    balance_date_time TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    amount            DOUBLE PRECISION NOT NULL,
    account_id        INT,
    FOREIGN KEY (account_id) REFERENCES account (account_id)
);
