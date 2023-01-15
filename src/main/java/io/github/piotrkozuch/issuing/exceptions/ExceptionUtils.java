package io.github.piotrkozuch.issuing.exceptions;

import jakarta.persistence.EntityNotFoundException;

import java.util.UUID;
import java.util.function.Supplier;

import static java.lang.String.format;

public class ExceptionUtils {

    public static Supplier<EntityNotFoundException> entityNotFoundException(UUID id) {
        return () -> new EntityNotFoundException(format("Unable to find entity with id %s", id));
    }
}
