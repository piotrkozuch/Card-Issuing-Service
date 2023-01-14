package io.github.piotrkozuch.issuing.cardholder.repository;

import io.github.piotrkozuch.issuing.model.Cardholder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CardholderRepository {

    Optional<Cardholder> findByEmail(String email);

    Optional<Cardholder> findById(UUID id);

    Cardholder save(Cardholder cardholder);

    List<Cardholder> findAll();

    void delete(UUID id);
}
