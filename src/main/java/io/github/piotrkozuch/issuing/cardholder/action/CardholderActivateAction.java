package io.github.piotrkozuch.issuing.cardholder.action;

import io.github.piotrkozuch.issuing.action.Action;
import io.github.piotrkozuch.issuing.cardholder.repository.CardholderRepository;
import io.github.piotrkozuch.issuing.cardholder.model.Cardholder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static io.github.piotrkozuch.issuing.utils.Checks.checkRequired;
import static java.time.Instant.now;

@Component
public class CardholderActivateAction implements Action<CardholderActivateAction.Params, Cardholder> {

    public static class Params {

        public final UUID cardholderId;

        public Params(UUID cardholderId) {
            this.cardholderId = checkRequired("cardholderId", cardholderId);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Params params = (Params) o;
            return cardholderId.equals(params.cardholderId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cardholderId);
        }
    }

    private final CardholderRepository cardholderRepository;

    @Autowired
    public CardholderActivateAction(CardholderRepository cardholderRepository) {
        this.cardholderRepository = checkRequired("cardholderRepository", cardholderRepository);
    }

    @Override
    @Transactional
    public Optional<Cardholder> execute(Params params) {
        final var cardholder = cardholderRepository.get(params.cardholderId);
        cardholder.setUpdatedDate(now());
        return Optional.of(cardholderRepository.save(cardholder.activate()));
    }
}
