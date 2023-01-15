package io.github.piotrkozuch.issuing.card.action.exception;

import io.github.piotrkozuch.issuing.common.types.CardBrand;

import static java.lang.String.format;

public class CardBrandNotSupportedException extends RuntimeException {

    public CardBrandNotSupportedException(CardBrand cardBrand) {
        super(format("Unsupported card brand: %s", cardBrand));
    }
}
