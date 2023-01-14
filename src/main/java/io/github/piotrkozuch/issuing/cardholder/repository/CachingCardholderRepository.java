package io.github.piotrkozuch.issuing.cardholder.repository;

import io.github.piotrkozuch.issuing.model.cardholder.Cardholder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.github.piotrkozuch.issuing.model.cardholder.CardholderState.DELETED;
import static io.github.piotrkozuch.issuing.utils.Checks.checkRequired;

public class CachingCardholderRepository implements CardholderRepository {

    private final CardholderJpaRepository cardholderJpaRepository;
    private final CardholderCache cardholderCache;

    public CachingCardholderRepository(CardholderJpaRepository cardholderJpaRepository, CardholderCache cardholderCache) {
        this.cardholderJpaRepository = checkRequired("cardholderJpaRepository", cardholderJpaRepository);
        this.cardholderCache = checkRequired("cardholderCache", cardholderCache);
    }

    @Override
    public Optional<Cardholder> findByEmail(String email) {
        return cardholderJpaRepository.findByEmailAndStateNot(email, DELETED);
    }

    @Override
    public Optional<Cardholder> findById(UUID id) {
        return cardholderCache.findById(id);
    }

    @Override
    public Cardholder save(Cardholder cardholder) {
        return cardholderJpaRepository.save(cardholder);
    }

    @Override
    public List<Cardholder> findAll() {
        return cardholderJpaRepository.findAllNotDeleted();
    }
}
