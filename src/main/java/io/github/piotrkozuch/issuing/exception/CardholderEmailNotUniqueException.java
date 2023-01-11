package io.github.piotrkozuch.issuing.exception;

public class CardholderEmailNotUniqueException extends RuntimeException {

    public CardholderEmailNotUniqueException() {
        super("Provided email is already in use.");
    }
}
