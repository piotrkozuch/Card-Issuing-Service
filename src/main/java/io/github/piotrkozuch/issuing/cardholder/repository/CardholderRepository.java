package io.github.piotrkozuch.issuing.cardholder.repository;

import io.github.piotrkozuch.issuing.cardholder.model.Cardholder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.github.piotrkozuch.issuing.cardholder.exception.CardholderExceptions.cardholderNotFoundException;

public interface CardholderRepository {

    default Cardholder get(UUID id) {
        return findById(id).orElseThrow(cardholderNotFoundException(id));
    }

    Optional<Cardholder> findByEmail(String email);

    Optional<Cardholder> findById(UUID id);

    Cardholder save(Cardholder cardholder);

    List<Cardholder> findAll();
}
