package io.github.piotrkozuch.issuing.card.service;

import com.neovisionaries.i18n.CurrencyCode;
import io.github.piotrkozuch.issuing.card.model.Card;
import io.github.piotrkozuch.issuing.common.types.CardBrand;
import io.github.piotrkozuch.issuing.common.types.CardType;

import java.util.UUID;

public interface CardService {

    Card issueNewCard(UUID cardholderId, CardBrand cardBrand, CardType cardType, CurrencyCode currency);

    Card get(UUID cardId);
}
