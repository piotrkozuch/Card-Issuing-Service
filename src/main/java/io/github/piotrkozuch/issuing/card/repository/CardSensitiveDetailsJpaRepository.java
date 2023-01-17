package io.github.piotrkozuch.issuing.card.repository;

import io.github.piotrkozuch.issuing.card.model.CardSensitiveDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CardSensitiveDetailsJpaRepository extends JpaRepository<CardSensitiveDetails, UUID> {

    Optional<CardSensitiveDetails> findByPan(String pan);
}
