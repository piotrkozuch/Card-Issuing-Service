package io.github.piotrkozuch.issuing.cardholder.exception;

import jakarta.persistence.EntityNotFoundException;

import java.util.UUID;
import java.util.function.Supplier;

import static java.lang.String.format;

public class CardholderExceptions {

    public static Supplier<EntityNotFoundException> cardholderNotFoundException(UUID id) {
        return () -> new EntityNotFoundException(format("Unable to find cardholder with id %s", id));
    }
}
