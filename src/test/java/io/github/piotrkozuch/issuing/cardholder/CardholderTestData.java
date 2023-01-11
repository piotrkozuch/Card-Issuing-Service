package io.github.piotrkozuch.issuing.cardholder;

import com.neovisionaries.i18n.CountryCode;
import io.github.piotrkozuch.issuing.model.Address;
import io.github.piotrkozuch.issuing.model.Cardholder;
import io.github.piotrkozuch.issuing.types.BillingAddress;
import io.github.piotrkozuch.issuing.types.CardholderState;

import java.time.LocalDate;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

public interface CardholderTestData {

    default Cardholder createCardholder() {
        final var cardholder = new Cardholder();
        cardholder.setId(randomUUID());
        cardholder.setCreatedDate(now());
        cardholder.setCreatedDate(now());
        cardholder.setState(CardholderState.PENDING);
        cardholder.setFirstName("Joe");
        cardholder.setLastName("Doe");
        cardholder.setBirthDate(LocalDate.now());
        cardholder.setEmail("joe.doe@test.com");
        cardholder.setPhone("+48700800900");

        final var address = new Address();
        address.setCity("Krakow");
        address.setCountry(CountryCode.PL);
        address.setStreetLine1("Grocka");
        address.setStreetLine1("1b");
        address.setPostcode("30-300");

        cardholder.setAddress(address);
        return cardholder;
    }

    default BillingAddress extractBillingAddress(Cardholder cardholder) {
        return new BillingAddress(
            cardholder.getAddress().getStreetLine1(),
            cardholder.getAddress().getStreetLine2(),
            cardholder.getAddress().getCity(),
            cardholder.getAddress().getCountry(),
            cardholder.getAddress().getPostcode()
        );
    }
}
