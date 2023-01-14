package io.github.piotrkozuch.issuing.cardholder.repository;

import io.github.piotrkozuch.issuing.cardholder.CardholderTestData;
import io.github.piotrkozuch.issuing.model.Cardholder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static io.github.piotrkozuch.issuing.model.CardholderState.DELETED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class CardholderCacheTest implements CardholderTestData {

    private final CardholderJpaRepository cardholderJpaRepository = mock(CardholderJpaRepository.class);
    private final CardholderCache cardholderCache = new CardholderCache(cardholderJpaRepository);

    @BeforeEach
    void setup() {
        reset(cardholderJpaRepository);
    }

    @Test
    void should_load_data_from_repository_if_not_available_in_cache() {
        // given
        var cardholder = createCardholder();

        given(cardholderJpaRepository.findByIdAndStateNot(cardholder.getId(), DELETED)).willReturn(Optional.of(cardholder));

        // when
        var result = cardholderCache.findById(cardholder.getId());

        assertThat(result).hasValue(cardholder);
        verify(cardholderJpaRepository).findByIdAndStateNot(cardholder.getId(), DELETED);
    }

    @Test
    void should_load_data_from_cache() {
        // given
        var cardholder = createCardholder();
        setupCache(cardholder);

        // when
        var result = cardholderCache.findById(cardholder.getId());

        assertThat(result).hasValue(cardholder);
        verifyNoInteractions(cardholderJpaRepository);
    }

    private void setupCache(Cardholder cardholder) {
        given(cardholderJpaRepository.findByIdAndStateNot(cardholder.getId(), DELETED)).willReturn(Optional.of(cardholder));
        cardholderCache.findById(cardholder.getId());

        reset(cardholderJpaRepository);
    }
}