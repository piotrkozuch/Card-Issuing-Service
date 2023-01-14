package io.github.piotrkozuch.issuing.cardholder;

import io.github.piotrkozuch.issuing.cardholder.exception.CardholderEmailNotUniqueException;
import io.github.piotrkozuch.issuing.cardholder.repository.CardholderRepository;
import io.github.piotrkozuch.issuing.model.Address;
import io.github.piotrkozuch.issuing.model.Cardholder;
import io.github.piotrkozuch.issuing.types.BillingAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static io.github.piotrkozuch.issuing.cardholder.exception.CardholderExceptions.cardholderNotFoundException;
import static io.github.piotrkozuch.issuing.model.CardholderState.PENDING;
import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

@Service
public class CardholderManager implements CardholderService {

    private CardholderRepository repository;

    @Autowired
    public CardholderManager(CardholderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Cardholder activateCardholder(UUID id) {
        final var cardholder = repository.findById(id).orElseThrow(cardholderNotFoundException(id));

        return repository.save(cardholder.activate());
    }

    @Override
    public Cardholder createCardholder(String firstName,
                                       String lastName,
                                       LocalDate birthDate,
                                       String email,
                                       String phone,
                                       BillingAddress billingAddress) {

        validate(email);

        final var timestamp = now();

        final var cardholder = new Cardholder();
        cardholder.setId(randomUUID());
        cardholder.setCreatedDate(timestamp);
        cardholder.setUpdatedDate(timestamp);
        cardholder.setState(PENDING);
        cardholder.setFirstName(firstName);
        cardholder.setLastName(lastName);
        cardholder.setBirthDate(birthDate);
        cardholder.setEmail(email);
        cardholder.setPhone(phone);
        cardholder.setAddress(addressFrom(billingAddress));

        return repository.save(cardholder);
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
        repository.delete(id);
    }

    private Address addressFrom(BillingAddress billingAddress) {
        final var address = new Address();
        address.setCountry(billingAddress.country);
        address.setCity(billingAddress.city);
        address.setStreetLine1(billingAddress.streetLine1);
        address.setPostcode(billingAddress.postcode);
        billingAddress.streetLine2.ifPresent(address::setStreetLine2);

        return address;
    }

    private void validate(String email) {
        if (repository.findByEmail(email).isPresent()) {
            throw new CardholderEmailNotUniqueException();
        }
    }
}
