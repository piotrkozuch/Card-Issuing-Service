CREATE TABLE cards_sensitive_details (
    id               UUID,
    pan              VARCHAR(19)     NOT NULL UNIQUE,
    cvv              VARCHAR(10)     NOT NULL,
    expiry_month     INTEGER         NOT NULL,
    expiry_year      INTEGER         NOT NULL,
    name_on_card     VARCHAR(100)    NOT NULL,
    created_date     TIMESTAMP       NOT NULL,
    updated_date     TIMESTAMP       NOT NULL,

    PRIMARY KEY(id, pan),
    CONSTRAINT unique_card UNIQUE (pan, cvv, expiry_month, expiry_year)
) PARTITION BY RANGE (pan);

CREATE INDEX IF NOT EXISTS cards_sensitive_details_created_date_index ON cards_sensitive_details(created_date);
CREATE UNIQUE INDEX IF NOT EXISTS cards_sensitive_details_pan_index ON cards_sensitive_details(pan);

CREATE TABLE visa_cards_sensitive_details PARTITION OF cards_sensitive_details
    FOR VALUES FROM ('400000000000000000') TO ('4999999999999999999');

CREATE TABLE mastercard_cards_sensitive_details PARTITION OF cards_sensitive_details
    FOR VALUES FROM ('510000000000000000') TO ('5199999999999999999');

CREATE TABLE maestro_cards_sensitive_details PARTITION OF cards_sensitive_details
    FOR VALUES FROM ('550000000000000000') TO ('5599999999999999999');
