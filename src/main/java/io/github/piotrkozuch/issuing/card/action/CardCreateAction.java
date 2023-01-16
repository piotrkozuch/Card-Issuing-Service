package io.github.piotrkozuch.issuing.card.action;

import com.neovisionaries.i18n.CurrencyCode;
import io.github.piotrkozuch.issuing.action.Action;
import io.github.piotrkozuch.issuing.card.action.exception.CardBrandNotSupportedException;
import io.github.piotrkozuch.issuing.card.action.exception.CardCurrencyNotSupportedException;
import io.github.piotrkozuch.issuing.card.action.exception.CardTypeNotSupportedException;
import io.github.piotrkozuch.issuing.card.model.Card;
import io.github.piotrkozuch.issuing.card.model.CardSensitiveDetails;
import io.github.piotrkozuch.issuing.card.repository.CardRepository;
import io.github.piotrkozuch.issuing.card.service.CardSensitiveDetailsService;
import io.github.piotrkozuch.issuing.cardholder.exception.CardholderInactiveException;
import io.github.piotrkozuch.issuing.cardholder.model.Cardholder;
import io.github.piotrkozuch.issuing.cardholder.repository.CardholderRepository;
import io.github.piotrkozuch.issuing.common.types.CardBrand;
import io.github.piotrkozuch.issuing.common.types.CardType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.neovisionaries.i18n.CurrencyCode.EUR;
import static com.neovisionaries.i18n.CurrencyCode.PLN;
import static io.github.piotrkozuch.issuing.card.model.CardState.INACTIVE;
import static io.github.piotrkozuch.issuing.card.service.CardSensitiveDetailsGenerator.maskedPan;
import static io.github.piotrkozuch.issuing.common.types.CardBrand.MAESTRO;
import static io.github.piotrkozuch.issuing.common.types.CardBrand.MASTERCARD;
import static io.github.piotrkozuch.issuing.common.types.CardBrand.VISA;
import static io.github.piotrkozuch.issuing.common.types.CardType.CREDIT;
import static io.github.piotrkozuch.issuing.common.types.CardType.DEBIT;
import static io.github.piotrkozuch.issuing.utils.Checks.checkRequired;
import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

@Component
public class CardCreateAction implements Action<CardCreateAction.Params, Card> {

    public static class Params {

        public final UUID cardholderId;
        public final CurrencyCode currency;
        public final CardBrand cardBrand;
        public final CardType cardType;

        public Params(UUID cardholderId, CurrencyCode currency, CardBrand cardBrand, CardType cardType) {
            this.cardholderId = checkRequired("cardholderId", cardholderId);
            this.currency = checkRequired("currency", currency);
            this.cardBrand = checkRequired("cardBrand", cardBrand);
            this.cardType = checkRequired("cardType", cardType);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Params params = (Params) o;
            return Objects.equals(cardholderId, params.cardholderId) && currency == params.currency && cardBrand == params.cardBrand && cardType == params.cardType;
        }

        @Override
        public int hashCode() {
            return Objects.hash(cardholderId, currency, cardBrand, cardType);
        }
    }

    private static final Set<CardBrand> SUPPORTED_CARD_BRANDS = Set.of(VISA, MASTERCARD, MAESTRO);
    private static final Set<CardType> SUPPORTED_CARD_TYPES = Set.of(DEBIT, CREDIT);
    private static final Set<CurrencyCode> SUPPORTED_CURRENCIES = Set.of(PLN, EUR);

    private final CardRepository cardRepository;
    private final CardholderRepository cardholderRepository;
    private final CardSensitiveDetailsService cardSensitiveDetailsGenerator;

    @Autowired
    public CardCreateAction(CardRepository cardRepository,
                            CardholderRepository cardholderRepository,
                            CardSensitiveDetailsService cardSensitiveDetailsGenerator) {
        this.cardRepository = checkRequired("cardRepository", cardRepository);
        this.cardholderRepository = checkRequired("cardholderRepository", cardholderRepository);
        this.cardSensitiveDetailsGenerator = checkRequired("cardSensitiveDetailsGenerator", cardSensitiveDetailsGenerator);
    }

    @Override
    @Transactional
    public Optional<Card> execute(Params params) {
        final var cardholder = cardholderRepository.get(params.cardholderId);

        validateCardholder(cardholder);

        final var createdDate = now();
        final var expiryMonth = cardSensitiveDetailsGenerator.generateExpiryMonth();
        final var expiryYear = cardSensitiveDetailsGenerator.generateExpiryYear();
        final var cvv = cardSensitiveDetailsGenerator.generateCvv();
        final var pan = cardSensitiveDetailsGenerator.generatePan(params.cardBrand);

        final var newCardDetails = cardRepository.save(
            createCardSensitiveDetaisl(cardholder, createdDate, expiryMonth, expiryYear, cvv, pan)
        );

        final var newCard = cardRepository.save(createCard(params, cardholder, createdDate, pan, newCardDetails));

        return Optional.of(newCard);
    }

    private Card createCard(Params params, Cardholder cardholder, Instant createdDate, String pan, CardSensitiveDetails newCardDetails) {
        final var newCard = new Card();
        newCard.setCardholderId(cardholder.getId());
        newCard.setId(randomUUID());
        newCard.setCreatedDate(createdDate);
        newCard.setUpdatedDate(createdDate);
        newCard.setBrand(checkCardBrand(params.cardBrand));
        newCard.setType(checkCardType(params.cardType));
        newCard.setCurrency(checkCardCurrency(params.currency));
        newCard.setState(INACTIVE);
        newCard.setMaskedPan(maskedPan(pan));
        newCard.setCardSensitiveDetails(newCardDetails);
        return newCard;
    }

    private CardSensitiveDetails createCardSensitiveDetaisl(Cardholder cardholder, Instant createdDate, int expiryMonth, int expiryYear, String cvv, String pan) {
        var newCardDetails = new CardSensitiveDetails();
        newCardDetails.setId(randomUUID());
        newCardDetails.setCreatedDate(createdDate);
        newCardDetails.setUpdatedDate(createdDate);
        newCardDetails.setNameOnCard(cardholder.getLegalName());
        newCardDetails.setExpiryMonth(expiryMonth);
        newCardDetails.setExpiryYear(expiryYear);
        newCardDetails.setCvv(cvv);
        newCardDetails.setPan(pan);
        return newCardDetails;
    }

    private CurrencyCode checkCardCurrency(CurrencyCode currency) {
        if (SUPPORTED_CURRENCIES.contains(currency)) {
            return currency;
        }

        throw new CardCurrencyNotSupportedException(currency);
    }

    private CardType checkCardType(CardType cardType) {
        if (SUPPORTED_CARD_TYPES.contains(cardType)) {
            return cardType;
        }
        throw new CardTypeNotSupportedException(cardType);
    }

    private CardBrand checkCardBrand(CardBrand cardBrand) {
        if (SUPPORTED_CARD_BRANDS.contains(cardBrand)) {
            return cardBrand;
        }

        throw new CardBrandNotSupportedException(cardBrand);
    }

    private void validateCardholder(Cardholder cardholder) {
        if (!cardholder.isActive()) {
            throw new CardholderInactiveException(cardholder.getId());
        }
    }
}
