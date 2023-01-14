CREATE TABLE cards_sensitive_details (
    id               UUID            PRIMARY KEY,
    card_id          UUID            NOT NULL UNIQUE,
    cvv              VARCHAR(10)     NOT NULL,
    expiry_month     INTEGER         NOT NULL,
    expiry_year      INTEGER         NOT NULL,
    name_on_card     VARCHAR(100)    NOT NULL,
    state            VARCHAR(40)     NOT NULL,
    created_date     TIMESTAMP       NOT NULL,
    updated_date     TIMESTAMP       NOT NULL,

    CONSTRAINT foreign_key_cards
    FOREIGN KEY (card_id) REFERENCES cards(id)
    ON DELETE CASCADE
);

CREATE UNIQUE INDEX IF NOT EXISTS cards_sensitive_details_card_id_index ON cards_sensitive_details(card_id);
CREATE INDEX IF NOT EXISTS cards_sensitive_details_created_date_index ON cards_sensitive_details(created_date);