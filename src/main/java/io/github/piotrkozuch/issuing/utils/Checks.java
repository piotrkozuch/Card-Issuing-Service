package io.github.piotrkozuch.issuing.utils;

import static java.lang.String.format;

public final class Checks {

    public static <T> T checkRequired(String paramName, T obj) {
        if (obj == null) {
            throw new IllegalArgumentException(format("Param '%s' is can't be null", paramName));
        }
        return obj;
    }
}
