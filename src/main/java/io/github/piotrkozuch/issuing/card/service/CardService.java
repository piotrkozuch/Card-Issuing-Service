package io.github.piotrkozuch.issuing.card.service;

import io.github.piotrkozuch.issuing.card.model.Card;
import io.github.piotrkozuch.issuing.common.types.CardBrand;
import io.github.piotrkozuch.issuing.common.types.CardType;
import io.github.piotrkozuch.issuing.common.types.Currency;

import java.util.UUID;

public interface CardService {

    Card issueNewCard(UUID cardholderId, CardBrand cardBrand, CardType cardType, Currency currency);

    Card get(UUID cardId, boolean includeSensitiveDetails);
}
