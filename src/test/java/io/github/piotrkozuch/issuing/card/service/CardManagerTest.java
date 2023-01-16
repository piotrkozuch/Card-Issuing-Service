package io.github.piotrkozuch.issuing.card.service;

import io.github.piotrkozuch.issuing.card.CardTestData;
import io.github.piotrkozuch.issuing.card.action.CardCreateAction;
import io.github.piotrkozuch.issuing.card.repository.CardRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class CardManagerTest implements CardTestData {

    private final CardRepository cardRepository = mock(CardRepository.class);
    private final CardCreateAction cardCreateAction = mock(CardCreateAction.class);
    private final CardManager cardManager = new CardManager(cardRepository, cardCreateAction);

    @Test
    void should_issue_new_card() {
        // given
        var card = createCard();

        given(cardCreateAction.execute(any())).willReturn(Optional.of(card));

        // when
        var newCard = cardManager.issueNewCard(card.getCardholderId(), card.getBrand(), card.getType(), card.getCurrency());

        // then
        assertThat(newCard).isNotNull();
        verify(cardCreateAction).execute(new CardCreateAction.Params(card.getCardholderId(), card.getCurrency(), card.getBrand(), card.getType()));
        verifyNoMoreInteractions(cardCreateAction);
        verifyNoInteractions(cardRepository);
    }

    @Test
    void should_return_card_by_id() {
        // given
        var card = createCard();
        given(cardRepository.getCard(card.getId())).willReturn(card);

        // when
        var loadedCard = cardManager.get(card.getId());

        // then
        assertThat(loadedCard).isEqualTo(card);
        verify(cardRepository).getCard(card.getId());
        verifyNoMoreInteractions(cardRepository);
        verifyNoInteractions(cardCreateAction);
    }

    @Test
    void should_return_all_cards() {
        // given
        var card1 = createCard();
        var card2 = createCard();
        given(cardRepository.findAll()).willReturn(List.of(card1, card2));

        // when
        var loadedCards = cardManager.findAll();

        // then
        assertThat(loadedCards.contains(card1)).isTrue();
        assertThat(loadedCards.contains(card2)).isTrue();

        verify(cardRepository).findAll();
        verifyNoMoreInteractions(cardRepository);
        verifyNoInteractions(cardCreateAction);
    }
}