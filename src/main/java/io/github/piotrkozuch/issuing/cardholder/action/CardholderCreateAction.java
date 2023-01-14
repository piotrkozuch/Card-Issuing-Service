package io.github.piotrkozuch.issuing.cardholder.action;

import io.github.piotrkozuch.issuing.action.Action;
import io.github.piotrkozuch.issuing.cardholder.exception.CardholderEmailNotUniqueException;
import io.github.piotrkozuch.issuing.cardholder.repository.CardholderRepository;
import io.github.piotrkozuch.issuing.model.Address;
import io.github.piotrkozuch.issuing.model.cardholder.Cardholder;
import io.github.piotrkozuch.issuing.types.BillingAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static io.github.piotrkozuch.issuing.model.cardholder.CardholderState.PENDING;
import static io.github.piotrkozuch.issuing.utils.Checks.checkRequired;
import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

@Component
public class CardholderCreateAction implements Action<CardholderCreateAction.Params, Cardholder> {

    public static class Params {

        public final String firstName;
        public final String lastName;
        public final LocalDate birthDate;
        public final String email;
        public final String phone;
        public final BillingAddress billingAddress;

        public Params(String firstName, String lastName, LocalDate birthDate, String email, String phone, BillingAddress billingAddress) {
            this.firstName = checkRequired("firstName", firstName);
            this.lastName = checkRequired("lastName", lastName);
            this.birthDate = checkRequired("birthDate", birthDate);
            this.email = checkRequired("email", email);
            this.phone = checkRequired("phone", phone);
            this.billingAddress = checkRequired("billingAddress", billingAddress);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Params params = (Params) o;
            return firstName.equals(params.firstName) && lastName.equals(params.lastName) && birthDate.equals(params.birthDate) && email.equals(params.email) && phone.equals(params.phone) && billingAddress.equals(params.billingAddress);
        }

        @Override
        public int hashCode() {
            return Objects.hash(firstName, lastName, birthDate, email, phone, billingAddress);
        }
    }

    private final CardholderRepository cardholderRepository;

    @Autowired
    public CardholderCreateAction(CardholderRepository cardholderRepository) {
        this.cardholderRepository = checkRequired("cardholderRepository", cardholderRepository);
    }

    @Override
    @Transactional
    public Optional<Cardholder> execute(Params params) {
        checkIfNotExists(params.email);
        final var timestamp = now();

        final var cardholder = new Cardholder();
        cardholder.setId(randomUUID());
        cardholder.setCreatedDate(timestamp);
        cardholder.setUpdatedDate(timestamp);
        cardholder.setState(PENDING);
        cardholder.setFirstName(params.firstName);
        cardholder.setLastName(params.lastName);
        cardholder.setBirthDate(params.birthDate);
        cardholder.setEmail(params.email);
        cardholder.setPhone(params.phone);
        cardholder.setAddress(addressFrom(params.billingAddress));

        return Optional.of(cardholderRepository.save(cardholder));
    }

    private void checkIfNotExists(String email) {
        if (cardholderRepository.findByEmail(email).isPresent()) {
            throw new CardholderEmailNotUniqueException();
        }
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
}
