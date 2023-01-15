package io.github.piotrkozuch.issuing.cardholder.exception;

import java.util.UUID;

import static java.lang.String.format;

public class CardholderInactiveException extends RuntimeException{

    public CardholderInactiveException(UUID cardholderId) {
        super(format("Cardholder %s is in inactive state", cardholderId));
    }
}
