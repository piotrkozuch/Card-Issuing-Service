package io.github.piotrkozuch.issuing.model;

import io.github.piotrkozuch.issuing.cardholder.CardholderTestData;
import io.github.piotrkozuch.issuing.cardholder.exception.CardholderChangeStateException;
import org.junit.jupiter.api.Test;

import static io.github.piotrkozuch.issuing.model.CardholderState.ACTIVE;
import static io.github.piotrkozuch.issuing.model.CardholderState.BLOCKED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CardholderTest implements CardholderTestData {

    @Test
    void should_activate_cardholder() {
        // given
        var cardholder = createCardholder();

        // when
        var result = cardholder.activate();

        // then
        assertThat(result.getState()).isEqualTo(ACTIVE);
    }

    @Test
    void should_not_activate_cardholder_if_not_in_pending_state() {
        // given
        var cardholder = createCardholder();
        cardholder.setState(BLOCKED);

        // expected
        assertThatThrownBy(cardholder::activate)
            .isInstanceOf(CardholderChangeStateException.class);

        assertThat(cardholder.getState()).isEqualTo(BLOCKED);
    }
}