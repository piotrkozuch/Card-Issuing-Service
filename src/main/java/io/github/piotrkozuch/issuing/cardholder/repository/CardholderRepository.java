package io.github.piotrkozuch.issuing.cardholder.repository;

import io.github.piotrkozuch.issuing.cardholder.model.Cardholder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.github.piotrkozuch.issuing.exceptions.ExceptionUtils.entityNotFoundException;

public interface CardholderRepository {

    default Cardholder get(UUID id) {
        return findById(id).orElseThrow(entityNotFoundException(id));
    }

    Optional<Cardholder> findByEmail(String email);

    Optional<Cardholder> findById(UUID id);

    Cardholder save(Cardholder cardholder);

    List<Cardholder> findAll();
}
