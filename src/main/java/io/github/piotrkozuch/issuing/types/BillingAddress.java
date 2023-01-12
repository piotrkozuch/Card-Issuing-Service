package io.github.piotrkozuch.issuing.types;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.neovisionaries.i18n.CountryCode;

import java.util.Optional;

import static io.github.piotrkozuch.issuing.utils.Checks.checkRequired;

@JsonDeserialize(builder = BillingAddress.Builder.class)
public class BillingAddress {

    public final String streetLine1;
    public final Optional<String> streetLine2;
    public final String city;
    public final CountryCode country;
    public final String postcode;

    private BillingAddress(Builder builder) {
        this.streetLine1 = checkRequired("streetLine1", builder.streetLine1);
        this.streetLine2 = Optional.ofNullable(builder.streetLine2);
        this.city = checkRequired("city", builder.city);
        this.country = checkRequired("country", builder.country);
        this.postcode = checkRequired("postcode", builder.postcode);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private String streetLine1;
        private String streetLine2;
        private String city;
        private CountryCode country;
        private String postcode;

        private Builder() {

        }

        public static Builder billingAddress() {
            return new Builder();
        }

        public BillingAddress build() {
            return new BillingAddress(this);
        }

        public Builder streetLine1(String streetLine1) {
            this.streetLine1 = streetLine1;
            return this;
        }

        public Builder streetLine2(String streetLine2) {
            this.streetLine2 = streetLine2;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder country(CountryCode country) {
            this.country = country;
            return this;
        }

        public Builder postcode(String postcode) {
            this.postcode = postcode;
            return this;
        }
    }
}
