package io.github.piotrkozuch.issuing.card.repository;

import io.github.piotrkozuch.issuing.card.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CardJpaRepository extends JpaRepository<Card, UUID> {

}
