package io.github.piotrkozuch.issuing.cardholder;

import io.github.piotrkozuch.issuing.cardholder.exception.CardholderEmailNotUniqueException;
import io.github.piotrkozuch.issuing.cardholder.repository.CardholderRepository;
import io.github.piotrkozuch.issuing.model.Cardholder;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static io.github.piotrkozuch.issuing.model.CardholderState.ACTIVE;
import static io.github.piotrkozuch.issuing.model.CardholderState.PENDING;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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
        var billingAddress = aBillingAddress().build();
        var cardholder = createCardholder(billingAddress);

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
        var billingAddress = aBillingAddress().build();
        var cardholder = createCardholder(billingAddress);

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

    @Test
    void should_activate_cardholder() {
        // given
        var cardholder = createCardholder();

        given(repository.findById(cardholder.getId())).willReturn(Optional.of(cardholder));
        given(repository.save(any())).willReturn(cardholder);

        // when
        var activeCardholder = manager.activateCardholder(cardholder.getId());

        // then
        assertThat(activeCardholder.hasState(ACTIVE)).isTrue();
        verify(repository).findById(cardholder.getId());
        verify(repository).save(activeCardholder);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void should_throw_exception_on_activation_when_cardholder_does_not_exist() {
        // given
        var cardholderId = randomUUID();

        given(repository.findById(cardholderId)).willReturn(Optional.empty());

        // expected
        assertThatThrownBy(() -> manager.activateCardholder(cardholderId))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void should_throw_exception_when_cardholder_does_not_exist() {
        // given
        var cardholderId = randomUUID();

        given(repository.findById(cardholderId)).willReturn(Optional.empty());

        // expected
        assertThatThrownBy(() -> manager.getCardholder(cardholderId))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void should_return_cardholder() {
        // given
        var cardholderId = randomUUID();

        given(repository.findById(cardholderId)).willReturn(Optional.empty());

        // expected
        assertThatThrownBy(() -> manager.getCardholder(cardholderId))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void should_return_all_cardholder() {
        // given
        var cardholder1 = createCardholder();
        var cardholder2 = createCardholder();

        given(repository.findAll()).willReturn(List.of(cardholder1, cardholder2));

        // when
        var cardholdersResponse = manager.getAllCardholders();

        // expected
        assertThat(cardholdersResponse.size()).isEqualTo(2);
        assertThat(cardholdersResponse.contains(cardholder1)).isTrue();
        assertThat(cardholdersResponse.contains(cardholder2)).isTrue();
    }

    @Test
    void should_delete_cardholder() {
        // given
        var cardholderId = randomUUID();

        // when
        manager.deleteCardholder(cardholderId);

        // then
        verify(repository).delete(cardholderId);
    }
}