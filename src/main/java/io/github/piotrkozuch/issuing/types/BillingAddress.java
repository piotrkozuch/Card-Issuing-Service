package io.github.piotrkozuch.issuing.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.neovisionaries.i18n.CountryCode;

import java.util.Optional;

import static io.github.piotrkozuch.issuing.utils.Checks.checkRequired;

public class BillingAddress {

    public final String streetLine1;
    public final Optional<String> streetLine2;
    public final String city;
    public final CountryCode country;
    public final String postcode;

    @JsonCreator
    public BillingAddress(String streetLine1,
                          String streetLine2,
                          String city,
                          CountryCode country,
                          String postcode) {
        this.streetLine1 = checkRequired("streetLine1", streetLine1);
        this.streetLine2 = Optional.ofNullable(streetLine2);
        this.city = checkRequired("city", city);
        this.country = checkRequired("country", country);
        this.postcode = checkRequired("postcode", postcode);
    }
}
