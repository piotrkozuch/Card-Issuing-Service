package io.github.piotrkozuch.issuing.card.action.exception;

import com.neovisionaries.i18n.CurrencyCode;

import static java.lang.String.format;

public class CardCurrencyNotSupportedException extends RuntimeException {

    public CardCurrencyNotSupportedException(CurrencyCode currency) {
        super(format("Unsupported card currency: %s", currency));
    }
}
