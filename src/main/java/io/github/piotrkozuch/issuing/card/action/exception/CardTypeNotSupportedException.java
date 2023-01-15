package io.github.piotrkozuch.issuing.card.action.exception;

import io.github.piotrkozuch.issuing.common.types.CardType;

import static java.lang.String.format;

public class CardTypeNotSupportedException extends RuntimeException {

    public CardTypeNotSupportedException(CardType cardType) {
        super(format("Unsupported card type: %s", cardType));
    }
}
