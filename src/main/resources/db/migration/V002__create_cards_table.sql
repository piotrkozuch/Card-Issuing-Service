CREATE TABLE cards (
    id              UUID            PRIMARY KEY,
    cardholder_id   UUID            NOT NULL,
    state           VARCHAR(40)     NOT NULL,
    masked_pan      VARCHAR(19)     NOT NULL,
    token           UUID            NOT NULL UNIQUE,
    type            VARCHAR(40)     NOT NULL,
    brand           VARCHAR(40)     NOT NULL,
    currency        VARCHAR(3)      NOT NULL,
    created_date    TIMESTAMP       NOT NULL,
    updated_date    TIMESTAMP       NOT NULL,

    CONSTRAINT foreign_key_cardholders
    FOREIGN KEY (cardholder_id) REFERENCES cardholders(id)
    ON DELETE CASCADE
);

CREATE UNIQUE INDEX IF NOT EXISTS cards_token_index ON cards(token);
CREATE INDEX IF NOT EXISTS cards_created_date_index ON cards(created_date);
CREATE INDEX IF NOT EXISTS cards_cardholder_id_index ON cards(cardholder_id);