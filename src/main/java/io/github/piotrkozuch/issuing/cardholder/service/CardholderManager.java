package io.github.piotrkozuch.issuing.cardholder.service;

import io.github.piotrkozuch.issuing.cardholder.action.CardholderActivateAction;
import io.github.piotrkozuch.issuing.cardholder.action.CardholderCreateAction;
import io.github.piotrkozuch.issuing.cardholder.action.CardholderDeleteAction;
import io.github.piotrkozuch.issuing.cardholder.repository.CardholderRepository;
import io.github.piotrkozuch.issuing.model.cardholder.Cardholder;
import io.github.piotrkozuch.issuing.types.BillingAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static io.github.piotrkozuch.issuing.cardholder.exception.CardholderExceptions.cardholderNotFoundException;
import static io.github.piotrkozuch.issuing.utils.Checks.checkRequired;

@Service
public class CardholderManager implements CardholderService {

    private final CardholderRepository repository;
    private final CardholderCreateAction cardholderCreateAction;
    private final CardholderActivateAction cardholderActivateAction;
    private final CardholderDeleteAction cardholderDeleteAction;

    @Autowired
    public CardholderManager(CardholderRepository repository,
                             CardholderCreateAction cardholderCreateAction,
                             CardholderActivateAction cardholderActivateAction,
                             CardholderDeleteAction cardholderDeleteAction) {
        this.repository = repository;
        this.cardholderCreateAction = checkRequired("cardholderCreateAction", cardholderCreateAction);
        this.cardholderActivateAction = checkRequired("cardholderActivateAction", cardholderActivateAction);
        this.cardholderDeleteAction = checkRequired("cardholderDeleteAction", cardholderDeleteAction);
    }

    @Override
    public Cardholder activateCardholder(UUID id) {
        return cardholderActivateAction.execute(new CardholderActivateAction.Params(id)).get();
    }

    @Override
    public Cardholder createCardholder(String firstName,
                                       String lastName,
                                       LocalDate birthDate,
                                       String email,
                                       String phone,
                                       BillingAddress billingAddress) {
        return cardholderCreateAction.execute(
            new CardholderCreateAction.Params(firstName, lastName, birthDate, email, phone, billingAddress)
        ).get();
    }

    @Override
    public Cardholder getCardholder(UUID id) {
        return repository.findById(id).orElseThrow(cardholderNotFoundException(id));
    }

    @Override
    public List<Cardholder> getAllCardholders() {
        return repository.findAll();
    }

    @Override
    public void deleteCardholder(UUID id) {
        cardholderDeleteAction.execute(new CardholderDeleteAction.Params(id));
    }
}
