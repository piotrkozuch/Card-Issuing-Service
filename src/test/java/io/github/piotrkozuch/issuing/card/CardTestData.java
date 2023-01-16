package io.github.piotrkozuch.issuing.card;

import com.neovisionaries.i18n.CurrencyCode;
import io.github.piotrkozuch.issuing.card.dto.CardCreateRequest;
import io.github.piotrkozuch.issuing.card.dto.CardResponse;
import io.github.piotrkozuch.issuing.card.dto.CardSensitiveDetailsResponse;
import io.github.piotrkozuch.issuing.card.model.Card;
import io.github.piotrkozuch.issuing.card.model.CardSensitiveDetails;
import io.github.piotrkozuch.issuing.card.model.CardState;
import io.github.piotrkozuch.issuing.common.types.CardBrand;
import io.github.piotrkozuch.issuing.common.types.CardType;

import java.time.LocalDate;

import static io.github.piotrkozuch.issuing.card.dto.CardCreateRequest.Builder.cardCreateRequest;
import static io.github.piotrkozuch.issuing.card.dto.CardResponse.Builder.cardResponse;
import static io.github.piotrkozuch.issuing.card.dto.CardSensitiveDetailsResponse.Builder.cardSensitiveDetailsResponse;
import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

public interface CardTestData {

    default Card createCard() {
        final var card = new Card();
        card.setId(randomUUID());
        card.setCardholderId(randomUUID());
        card.setMaskedPan("424242******4242");
        card.setToken(randomUUID());
        card.setState(CardState.ACTIVE);
        card.setType(CardType.DEBIT);
        card.setBrand(CardBrand.VISA);
        card.setCreatedDate(now());
        card.setUpdatedDate(now());
        card.setCurrency(CurrencyCode.EUR);
        return card;
    }

    default CardSensitiveDetails createCardSensitiveDetails(){
        final var cardDetails = new CardSensitiveDetails();
        cardDetails.setId(randomUUID());
        cardDetails.setPan("4242424242424242");
        cardDetails.setCvv("123");
        cardDetails.setCreatedDate(now());
        cardDetails.setUpdatedDate(now());
        cardDetails.setNameOnCard("Joe Doe");
        return cardDetails;
    }

    default CardCreateRequest.Builder aCardCreateRequest() {
        return cardCreateRequest()
            .cardBrand(CardBrand.VISA)
            .cardType(CardType.DEBIT)
            .currency(CurrencyCode.EUR)
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
            .currency(CurrencyCode.PLN);
    }

    default CardSensitiveDetailsResponse.Builder aCardSensitiveDetailsResponse() {
        return cardSensitiveDetailsResponse()
            .id(randomUUID())
            .cvv("123")
            .pan("4242424242424242")
            .createdDate(now())
            .updatedDate(now())
            .expiryMonth(1)
            .expiryYear(LocalDate.now().getYear() + 3)
            .nameOnCard("Joe Doe");
    }
}
