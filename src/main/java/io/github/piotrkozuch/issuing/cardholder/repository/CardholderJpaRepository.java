package io.github.piotrkozuch.issuing.cardholder.repository;

import io.github.piotrkozuch.issuing.model.Cardholder;
import io.github.piotrkozuch.issuing.model.CardholderState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardholderJpaRepository extends JpaRepository<Cardholder, UUID> {

    Optional<Cardholder> findByEmailAndStateNot(String email, CardholderState state);

    Optional<Cardholder> findByIdAndStateNot(UUID id, CardholderState state);

    @Query(value = "SELECT * FROM cardholders WHERE state != 'DELETED'", nativeQuery = true)
    List<Cardholder> findAllNotDeleted();
}
