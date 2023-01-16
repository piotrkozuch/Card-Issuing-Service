CREATE TABLE cards_sensitive_details (
    id               UUID            PRIMARY KEY,
    pan              VARCHAR(19)     NOT NULL UNIQUE,
    cvv              VARCHAR(10)     NOT NULL,
    expiry_month     INTEGER         NOT NULL,
    expiry_year      INTEGER         NOT NULL,
    name_on_card     VARCHAR(100)    NOT NULL,
    created_date     TIMESTAMP       NOT NULL,
    updated_date     TIMESTAMP       NOT NULL,

    CONSTRAINT unique_card UNIQUE (pan, cvv, expiry_month, expiry_year)
);

CREATE INDEX IF NOT EXISTS cards_sensitive_details_created_date_index ON cards_sensitive_details(created_date);