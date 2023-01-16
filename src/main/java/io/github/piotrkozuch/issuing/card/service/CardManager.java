package io.github.piotrkozuch.issuing.card.service;

import com.neovisionaries.i18n.CurrencyCode;
import io.github.piotrkozuch.issuing.card.action.CardCreateAction;
import io.github.piotrkozuch.issuing.card.model.Card;
import io.github.piotrkozuch.issuing.card.repository.CardRepository;
import io.github.piotrkozuch.issuing.common.types.CardBrand;
import io.github.piotrkozuch.issuing.common.types.CardType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static io.github.piotrkozuch.issuing.utils.Checks.checkRequired;

@Service
public class CardManager implements CardService {

    private final CardRepository cardRepository;
    private final CardCreateAction cardCreateAction;

    @Autowired
    public CardManager(CardRepository cardRepository,
                       CardCreateAction cardCreateAction) {
        this.cardRepository = checkRequired("cardRepository", cardRepository);
        this.cardCreateAction = checkRequired("cardCreateAction", cardCreateAction);
    }

    @Override
    public Card issueNewCard(UUID cardholderId, CardBrand cardBrand, CardType cardType, CurrencyCode currency) {
        return cardCreateAction.execute(new CardCreateAction.Params(
            cardholderId, currency, cardBrand, cardType
        )).get();
    }

    @Override
    public Card get(UUID cardId) {
        return cardRepository.getCard(cardId);
    }

    public List<Card> findAll() {
        return cardRepository.findAll();
    }
}
