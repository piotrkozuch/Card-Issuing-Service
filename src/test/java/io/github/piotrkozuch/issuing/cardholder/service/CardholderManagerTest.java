package io.github.piotrkozuch.issuing.cardholder.service;

import io.github.piotrkozuch.issuing.cardholder.CardholderTestData;
import io.github.piotrkozuch.issuing.cardholder.action.CardholderActivateAction;
import io.github.piotrkozuch.issuing.cardholder.action.CardholderCreateAction;
import io.github.piotrkozuch.issuing.cardholder.action.CardholderDeleteAction;
import io.github.piotrkozuch.issuing.cardholder.repository.CardholderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CardholderManagerTest implements CardholderTestData {

    private final CardholderRepository repository = mock(CardholderRepository.class);
    private final CardholderCreateAction cardholderCreateAction = mock(CardholderCreateAction.class);
    private final CardholderActivateAction cardholderActivateAction = mock(CardholderActivateAction.class);
    private final CardholderDeleteAction cardholderDeleteAction = mock(CardholderDeleteAction.class);
    private final CardholderManager manager = new CardholderManager(repository,
        cardholderCreateAction,
        cardholderActivateAction,
        cardholderDeleteAction);

    @Test
    void should_activate_cardholder() {
        // given
        var cardholder = createCardholder().activate();
        given(cardholderActivateAction.execute(any())).willReturn(Optional.of(cardholder));

        // when
        manager.activateCardholder(cardholder.getId());

        // then
        verify(cardholderActivateAction).execute(new CardholderActivateAction.Params(cardholder.getId()));
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
        verify(cardholderDeleteAction).execute(new CardholderDeleteAction.Params(cardholderId));
    }
}