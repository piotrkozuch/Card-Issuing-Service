package io.github.piotrkozuch.issuing.cardholder;

import io.github.piotrkozuch.issuing.exception.CardholderEmailNotUniqueException;
import io.github.piotrkozuch.issuing.model.Cardholder;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static io.github.piotrkozuch.issuing.types.CardholderState.PENDING;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class CardholderManagerTest implements CardholderTestData {

    private final CardholderRepository repository = mock(CardholderRepository.class);
    private final CardholderManager manager = new CardholderManager(repository);

    @Test
    void should_throw_exception_if_cardholder_email_is_not_unique() {
        // given
        var cardholder = createCardholder();
        var billingAddress = extractBillingAddress(cardholder);

        given(repository.findByEmail(cardholder.getEmail())).willReturn(Optional.of(cardholder));

        // when
        assertThatThrownBy(() -> manager.createCardholder(
                cardholder.getFirstName(),
                cardholder.getLastName(),
                cardholder.getBirthDate(),
                cardholder.getEmail(),
                cardholder.getPhone(),
                billingAddress)
            // then
        ).isInstanceOf(CardholderEmailNotUniqueException.class);

        verify(repository).findByEmail(cardholder.getEmail());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void should_create_new_cardholder() {
        // given
        var cardholder = createCardholder();
        var billingAddress = extractBillingAddress(cardholder);

        given(repository.findByEmail(cardholder.getEmail())).willReturn(Optional.empty());

        // when
        manager.createCardholder(
            cardholder.getFirstName(),
            cardholder.getLastName(),
            cardholder.getBirthDate(),
            cardholder.getEmail(),
            cardholder.getPhone(),
            billingAddress);

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