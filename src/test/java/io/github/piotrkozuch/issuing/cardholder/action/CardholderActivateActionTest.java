package io.github.piotrkozuch.issuing.cardholder.action;

import io.github.piotrkozuch.issuing.cardholder.CardholderTestData;
import io.github.piotrkozuch.issuing.cardholder.repository.CardholderRepository;
import io.github.piotrkozuch.issuing.model.Cardholder;
import org.junit.jupiter.api.Test;

import static io.github.piotrkozuch.issuing.model.CardholderState.ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class CardholderActivateActionTest implements CardholderTestData {

    private final CardholderRepository repository = mock(CardholderRepository.class);
    private final CardholderActivateAction action = new CardholderActivateAction(repository);

    @Test
    void should_activate_cardholder() {
        // given
        var pendingCardholder = createCardholder();
        given(repository.get(pendingCardholder.getId())).willReturn(pendingCardholder);
        given(repository.save(any())).willReturn(pendingCardholder);

        // when
        var result = action.execute(new CardholderActivateAction.Params(pendingCardholder.getId()));

        assertThat(result).isPresent();
        assertThat(result.get().getState()).isEqualTo(ACTIVE);

        verify(repository).get(pendingCardholder.getId());
        verify(repository).save(any(Cardholder.class));
        verifyNoMoreInteractions(repository);
    }
}