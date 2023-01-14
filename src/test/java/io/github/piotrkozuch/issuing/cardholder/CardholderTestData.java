package io.github.piotrkozuch.issuing.cardholder;

import io.github.piotrkozuch.issuing.dto.CardholderCreateRequest;
import io.github.piotrkozuch.issuing.model.Address;
import io.github.piotrkozuch.issuing.model.Cardholder;
import io.github.piotrkozuch.issuing.model.CardholderState;
import io.github.piotrkozuch.issuing.types.BillingAddress;

import java.time.LocalDate;
import java.util.Random;

import static com.neovisionaries.i18n.CountryCode.PL;
import static io.github.piotrkozuch.issuing.dto.CardholderCreateRequest.Builder.cardholderCreateRequest;
import static io.github.piotrkozuch.issuing.types.BillingAddress.Builder.billingAddress;
import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

public interface CardholderTestData {

    default Cardholder createCardholder() {
        return createCardholder(aBillingAddress().build());
    }

    default Cardholder createCardholder(BillingAddress billingAddress) {
        final var cardholder = new Cardholder();
        cardholder.setId(randomUUID());
        cardholder.setCreatedDate(now());
        cardholder.setUpdatedDate(now());
        cardholder.setState(CardholderState.PENDING);
        cardholder.setFirstName("Joe");
        cardholder.setLastName("Doe");
        cardholder.setBirthDate(LocalDate.now());
        cardholder.setEmail("joe.doe@test" + new Random().nextInt() + ".com");
        cardholder.setPhone("+48700800900");

        final var address = new Address();
        address.setCity(billingAddress.city);
        address.setCountry(billingAddress.country);
        address.setStreetLine1(billingAddress.streetLine1);
        billingAddress.streetLine2.ifPresent(address::setStreetLine2);
        address.setPostcode(billingAddress.postcode);

        cardholder.setAddress(address);
        return cardholder;
    }

    default BillingAddress.Builder aBillingAddress() {
        return billingAddress()
            .streetLine1("Grocka")
            .streetLine2("1")
            .city("Kraków")
            .country(PL)
            .postcode("30-300");
    }

    default CardholderCreateRequest.Builder aCardholderCreateRequest() {
        return cardholderCreateRequest()
            .firstName("Joe")
            .lastName("Doe")
            .birthDate(LocalDate.of(1988, 6, 27))
            .phone("+49700900800")
            .email("joe.doe@example.com")
            .billingAddress(aBillingAddress().build());
    }
}
