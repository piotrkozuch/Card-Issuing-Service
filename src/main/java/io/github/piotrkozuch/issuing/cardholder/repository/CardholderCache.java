package io.github.piotrkozuch.issuing.cardholder.repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.piotrkozuch.issuing.cardholder.model.Cardholder;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static io.github.piotrkozuch.issuing.cardholder.exception.CardholderExceptions.cardholderNotFoundException;
import static io.github.piotrkozuch.issuing.cardholder.model.CardholderState.DELETED;
import static io.github.piotrkozuch.issuing.utils.Checks.checkRequired;

public class CardholderCache {

    private final CardholderJpaRepository repository;
    private final Cache<UUID, Cardholder> cardholderCache;

    public CardholderCache(CardholderJpaRepository repository) {
        this.repository = checkRequired("repository", repository);
        this.cardholderCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.of(10, ChronoUnit.SECONDS))
            .maximumSize(100)
            .build();
    }

    public Optional<Cardholder> findById(UUID cardholderId) {
        final var cardholder = cardholderCache.get(cardholderId, id -> repository.findByIdAndStateNot(id, DELETED).
            orElseThrow(cardholderNotFoundException(id)));

        return Optional.of(cardholder);
    }
}
