package io.github.piotrkozuch.issuing.card.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.github.piotrkozuch.issuing.card.service.LuhnCheck.calculateControlDigit;
import static io.github.piotrkozuch.issuing.card.service.LuhnCheck.isValidCardNumber;
import static org.assertj.core.api.Assertions.assertThat;

class LuhnCheckTest {

    @ParameterizedTest
    @CsvSource({
        "4242424242424242, true",
        "4111111111111114, false",
    })
    void should_return_correct_result_if_card_number_is_correct(String cardNumber, boolean expected) {
        assertThat(isValidCardNumber(cardNumber)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
        "424242424242424, 2",
        "411111111111111, 1",
    })
    void should_calculate_control_digit_for_given_payload(String payload, int expectedDigit) {
        assertThat(calculateControlDigit(payload)).isEqualTo(expectedDigit);
    }
}