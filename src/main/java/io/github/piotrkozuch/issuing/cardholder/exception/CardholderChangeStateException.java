package io.github.piotrkozuch.issuing.cardholder.exception;

import io.github.piotrkozuch.issuing.model.CardholderState;

import java.util.UUID;

import static java.lang.String.format;

public class CardholderChangeStateException extends RuntimeException {

    public CardholderChangeStateException(UUID id, CardholderState currentState, CardholderState expectedState) {
        super(format("Unable to change cardholder '%s' state from '%s' to '%s'", id, currentState, expectedState));
    }
}
