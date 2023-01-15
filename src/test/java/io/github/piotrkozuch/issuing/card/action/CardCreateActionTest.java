package io.github.piotrkozuch.issuing.card.action;

import io.github.piotrkozuch.issuing.card.repository.CardRepository;
import io.github.piotrkozuch.issuing.card.service.CardSensitiveDetailsService;
import io.github.piotrkozuch.issuing.cardholder.repository.CardholderRepository;

import static org.mockito.Mockito.mock;

class CardCreateActionTest {

    private final CardRepository cardRepository = mock(CardRepository.class);
    private final CardholderRepository cardholderRepository = mock(CardholderRepository.class);
    private final CardSensitiveDetailsService cardSensitiveDetailsService = mock(CardSensitiveDetailsService.class);

    private final CardCreateAction action = new CardCreateAction(cardRepository, cardholderRepository, cardSensitiveDetailsService);

    //TODO: implement tests
}