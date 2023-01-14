package io.github.piotrkozuch.issuing.cardholder.action;

import io.github.piotrkozuch.issuing.cardholder.CardholderTestData;
import io.github.piotrkozuch.issuing.cardholder.exception.CardholderEmailNotUniqueException;
import io.github.piotrkozuch.issuing.cardholder.repository.CardholderRepository;
import io.github.piotrkozuch.issuing.model.cardholder.Cardholder;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static io.github.piotrkozuch.issuing.model.cardholder.CardholderState.PENDING;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class CardholderCreateActionTest implements CardholderTestData {

    private final CardholderRepository repository = mock(CardholderRepository.class);
    private final CardholderCreateAction action = new CardholderCreateAction(repository);

    @Test
    void should_throw_exception_if_cardholder_email_is_not_unique() {
        // given
        var billingAddress = aBillingAddress().build();
        var cardholder = createCardholder(billingAddress);

        given(repository.findByEmail(cardholder.getEmail())).willReturn(Optional.of(cardholder));

        // when
        assertThatThrownBy(() -> action.execute(new CardholderCreateAction.Params(
                cardholder.getFirstName(),
                cardholder.getLastName(),
                cardholder.getBirthDate(),
                cardholder.getEmail(),
                cardholder.getPhone(),
                billingAddress))
            // then
        ).isInstanceOf(CardholderEmailNotUniqueException.class);

        verify(repository).findByEmail(cardholder.getEmail());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void should_create_new_cardholder() {
        // given
        var billingAddress = aBillingAddress().build();
        var cardholder = createCardholder(billingAddress);

        given(repository.findByEmail(cardholder.getEmail())).willReturn(Optional.empty());
        when(repository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // when
        action.execute(new CardholderCreateAction.Params(
            cardholder.getFirstName(),
            cardholder.getLastName(),
            cardholder.getBirthDate(),
            cardholder.getEmail(),
            cardholder.getPhone(),
            billingAddress));

        // then
        var captor = ArgumentCaptor.forClass(Cardholder.class);
        verify(repository).save(captor.capture());

        var savedCardholder = captor.getValue();
        assertThat(savedCardholder.getFirstName()).isEqualTo(cardholder.getFirstName());
        assertThat(savedCardholder.getLastName()).isEqualTo(cardholder.getLastName());
        assertThat(savedCardholder.getBirthDate()).isEqualTo(cardholder.getBirthDate());
        assertThat(savedCardholder.getEmail()).isEqualTo(cardholder.getEmail());
        assertThat(savedCardholder.getPhone()).isEqualTo(cardholder.getPhone());
        assertThat(savedCardholder.getAddress()).isEqualTo(cardholder.getAddress());
        assertThat(savedCardholder.getId()).isNotNull();
        assertThat(savedCardholder.getCreatedDate()).isNotNull();
        assertThat(savedCardholder.getUpdatedDate()).isNotNull();
        assertThat(savedCardholder.getState()).isEqualTo(PENDING);

        verify(repository).findByEmail(cardholder.getEmail());
        verifyNoMoreInteractions(repository);
    }

}