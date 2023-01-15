package io.github.piotrkozuch.issuing.card.action.exception;

import io.github.piotrkozuch.issuing.common.types.Currency;

import static java.lang.String.format;

public class CardCurrencyNotSupportedException extends RuntimeException {

    public CardCurrencyNotSupportedException(Currency currency) {
        super(format("Unsupported card currency: %s", currency));
    }
}
