package io.github.piotrkozuch.issuing.card.service;

import io.github.piotrkozuch.issuing.card.action.exception.CardBrandNotSupportedException;
import io.github.piotrkozuch.issuing.card.repository.CardRepository;
import io.github.piotrkozuch.issuing.common.types.CardBrand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static io.github.piotrkozuch.issuing.common.types.CardBrand.AMEX;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class CardSensitiveDetailsGeneratorTest {

    private final CardRepository cardRepository = mock(CardRepository.class);
    private final CardSensitiveDetailsService generator = new CardSensitiveDetailsGenerator(cardRepository);

    @Test
    void should_generate_random_cvv() {
        // when
        var cvv = generator.generateCvv();

        // then
        assertThat(cvv.length()).isEqualTo(3);
        assertThatCode(() -> parseInt(cvv)).doesNotThrowAnyException();
    }

    @Test
    void should_generate_expiry_month() {
        // when
        var month = generator.generateExpiryMonth();

        // then
        assertThat(month).isEqualTo(LocalDate.now().getMonthValue());
    }

    @Test
    void should_generate_expiry_year() {
        // when
        var month = generator.generateExpiryYear();

        // then
        assertThat(month).isEqualTo(LocalDate.now().getYear() + 3);
    }

    @Test
    void should_throw_exception_if_card_brand_is_not_supported() {
        // given
        var cardBrand = AMEX;

        // expected
        assertThatThrownBy(() -> generator.generatePan(cardBrand))
            .isInstanceOf(CardBrandNotSupportedException.class);
    }

    @ParameterizedTest
    @CsvSource({
        "VISA, 4",
        "MASTERCARD, 51",
        "MAESTRO, 55"
    })
    void should_generate_random_PAN(CardBrand cardBrand, String startsWith) {
        // expected
        var pan = generator.generatePan(cardBrand);

        // then
        assertThatCode(() -> parseLong(pan)).doesNotThrowAnyException();
        assertThat(pan).startsWith(startsWith);
    }
}