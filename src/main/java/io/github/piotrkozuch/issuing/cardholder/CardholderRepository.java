package io.github.piotrkozuch.issuing.cardholder;

import io.github.piotrkozuch.issuing.model.Cardholder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardholderRepository extends JpaRepository<Cardholder, UUID> {

    Optional<Cardholder> findByEmail(String email);
}
