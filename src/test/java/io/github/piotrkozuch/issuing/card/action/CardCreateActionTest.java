package io.github.piotrkozuch.issuing.card.action;

import io.github.piotrkozuch.issuing.card.CardTestData;
import io.github.piotrkozuch.issuing.card.action.exception.CardBrandNotSupportedException;
import io.github.piotrkozuch.issuing.card.action.exception.CardCurrencyNotSupportedException;
import io.github.piotrkozuch.issuing.card.action.exception.CardTypeNotSupportedException;
import io.github.piotrkozuch.issuing.card.model.Card;
import io.github.piotrkozuch.issuing.card.repository.CardRepository;
import io.github.piotrkozuch.issuing.card.service.CardSensitiveDetailsService;
import io.github.piotrkozuch.issuing.cardholder.CardholderTestData;
import io.github.piotrkozuch.issuing.cardholder.exception.CardholderInactiveException;
import io.github.piotrkozuch.issuing.cardholder.repository.CardholderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.neovisionaries.i18n.CurrencyCode.EUR;
import static com.neovisionaries.i18n.CurrencyCode.SGD;
import static io.github.piotrkozuch.issuing.card.model.CardState.INACTIVE;
import static io.github.piotrkozuch.issuing.common.types.CardBrand.AMEX;
import static io.github.piotrkozuch.issuing.common.types.CardBrand.VISA;
import static io.github.piotrkozuch.issuing.common.types.CardType.DEBIT;
import static io.github.piotrkozuch.issuing.common.types.CardType.PREPAID;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CardCreateActionTest implements CardTestData, CardholderTestData {

    private final CardRepository cardRepository = mock(CardRepository.class);
    private final CardholderRepository cardholderRepository = mock(CardholderRepository.class);
    private final CardSensitiveDetailsService cardSensitiveDetailsService = mock(CardSensitiveDetailsService.class);

    private final CardCreateAction action = new CardCreateAction(cardRepository, cardholderRepository, cardSensitiveDetailsService);

    @BeforeEach
    void setup() {
        given(cardSensitiveDetailsService.generateExpiryMonth()).willReturn(10);
        given(cardSensitiveDetailsService.generateExpiryYear()).willReturn(2026);
        given(cardSensitiveDetailsService.generateCvv()).willReturn("123");
        given(cardSensitiveDetailsService.generatePan(VISA)).willReturn("4242424242424242");
    }

    @Test
    void should_throw_exception_if_cardholder_is_not_active() {
        // given
        var cardholder = createCardholder();
        given(cardholderRepository.get(cardholder.getId())).willReturn(cardholder);

        // expected
        assertThatThrownBy(() -> action.execute(new CardCreateAction
            .Params(cardholder.getId(), EUR, VISA, DEBIT)))
            .isInstanceOf(CardholderInactiveException.class)
            .hasMessage(format("Cardholder %s is in inactive state", cardholder.getId()));
    }

    @Test
    void should_throw_exception_if_card_brand_is_not_supported() {
        // given
        var cardholder = createCardholder().activate();
        given(cardholderRepository.get(cardholder.getId())).willReturn(cardholder);

        // expected
        assertThatThrownBy(() -> action.execute(new CardCreateAction
            .Params(cardholder.getId(), EUR, AMEX, DEBIT)))
            .isInstanceOf(CardBrandNotSupportedException.class)
            .hasMessage("Unsupported card brand: AMEX");
    }

    @Test
    void should_throw_exception_if_card_type_is_not_supported() {
        // given
        var cardholder = createCardholder().activate();
        given(cardholderRepository.get(cardholder.getId())).willReturn(cardholder);

        // expected
        assertThatThrownBy(() -> action.execute(new CardCreateAction
            .Params(cardholder.getId(), EUR, VISA, PREPAID)))
            .isInstanceOf(CardTypeNotSupportedException.class)
            .hasMessage("Unsupported card type: PREPAID");
    }

    @Test
    void should_throw_exception_if_card_currency_is_not_supported() {
        // given
        var cardholder = createCardholder().activate();
        given(cardholderRepository.get(cardholder.getId())).willReturn(cardholder);

        // expected
        assertThatThrownBy(() -> action.execute(new CardCreateAction
            .Params(cardholder.getId(), SGD, VISA, DEBIT)))
            .isInstanceOf(CardCurrencyNotSupportedException.class)
            .hasMessage("Unsupported card currency: SGD");
    }

    @Test
    void should_issue_new_card() {
        // given
        var cardholder = createCardholder().activate();
        given(cardholderRepository.get(cardholder.getId())).willReturn(cardholder);
        given(cardRepository.save(any(Card.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        var newCard = action.execute(new CardCreateAction
            .Params(cardholder.getId(), EUR, VISA, DEBIT)).get();

        // then
        assertThat(newCard.getId()).isNotNull();
        assertThat(newCard.getCardholderId()).isEqualTo(cardholder.getId());
        assertThat(newCard.getState()).isEqualTo(INACTIVE);
        assertThat(newCard.getBrand()).isEqualTo(VISA);
        assertThat(newCard.getType()).isEqualTo(DEBIT);
        assertThat(newCard.getCurrency()).isEqualTo(EUR);
    }
}