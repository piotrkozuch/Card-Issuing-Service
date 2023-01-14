package io.github.piotrkozuch.issuing.card;

import io.github.piotrkozuch.issuing.card.dto.CardCreateRequest;
import io.github.piotrkozuch.issuing.card.dto.CardResponse;
import io.github.piotrkozuch.issuing.card.dto.CardSensitiveDetailsResponse;
import io.github.piotrkozuch.issuing.card.model.CardSensitiveDetailsState;
import io.github.piotrkozuch.issuing.common.types.CardBrand;
import io.github.piotrkozuch.issuing.common.types.CardType;
import io.github.piotrkozuch.issuing.common.types.Currency;

import static io.github.piotrkozuch.issuing.card.dto.CardCreateRequest.Builder.cardCreateRequest;
import static io.github.piotrkozuch.issuing.card.dto.CardResponse.Builder.cardResponse;
import static io.github.piotrkozuch.issuing.card.dto.CardSensitiveDetailsResponse.Builder.cardSensitiveDetailsResponse;
import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

public interface CardTestData {

    default CardCreateRequest.Builder aCardCreateRequest() {
        return cardCreateRequest()
            .cardBrand(CardBrand.VISA)
            .cardType(CardType.PREPAID)
            .currency(Currency.EUR)
            .cardholderId(randomUUID());
    }

    default CardResponse.Builder aCardResponse() {
        return cardResponse()
            .id(randomUUID())
            .createdDate(now())
            .updatedDate(now())
            .cardholderId(randomUUID())
            .maskedPan("424242******4242")
            .brand(CardBrand.VISA)
            .type(CardType.PREPAID)
            .currency(Currency.PLN);
    }

    default CardSensitiveDetailsResponse.Builder aCardSensitiveDetailsResponse() {
        return cardSensitiveDetailsResponse()
            .id(randomUUID())
            .cvv("123")
            .pan("4242424242424242")
            .createdDate(now())
            .updatedDate(now())
            .expiryMonth(1)
            .expiryYear(2030)
            .nameOnCard("Joe Doe")
            .state(CardSensitiveDetailsState.ACTIVE.name());
    }
}
