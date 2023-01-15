package io.github.piotrkozuch.issuing.card.repository;

import io.github.piotrkozuch.issuing.card.model.Card;
import io.github.piotrkozuch.issuing.card.model.CardSensitiveDetails;

import java.util.List;
import java.util.UUID;

import static io.github.piotrkozuch.issuing.exceptions.ExceptionUtils.entityNotFoundException;
import static io.github.piotrkozuch.issuing.utils.Checks.checkRequired;

public class CardRepository {

    private final CardJpaRepository cardJpaRepository;
    private final CardSensitiveDetailsJpaRepository cardSensitiveDetailsJpaRepository;

    public CardRepository(CardJpaRepository cardJpaRepository,
                          CardSensitiveDetailsJpaRepository cardSensitiveDetailsJpaRepository) {
        this.cardJpaRepository = checkRequired("cardJpaRepository", cardJpaRepository);
        this.cardSensitiveDetailsJpaRepository = checkRequired("cardSensitiveDetailsJpaRepository", cardSensitiveDetailsJpaRepository);
    }

    public Card save(Card card) {
        return cardJpaRepository.save(card);
    }

    public CardSensitiveDetails saveCardSensitiveDetails(CardSensitiveDetails cardSensitiveDetails) {
        return cardSensitiveDetailsJpaRepository.save(cardSensitiveDetails);
    }

    public Card getCard(UUID cardId, boolean includeSensitiveDetails) {
        final var card = cardJpaRepository.findById(cardId)
            .orElseThrow(entityNotFoundException(cardId));

        if (includeSensitiveDetails) {
            final var sensitiveDetails = cardSensitiveDetailsJpaRepository.findById(card.getToken())
                .orElseThrow(entityNotFoundException(cardId));
            card.setCardSensitiveDetails(sensitiveDetails);
        }

        return card;
    }

    public List<Card> findAll() {
        return cardJpaRepository.findAll();
    }
}
