package io.github.piotrkozuch.issuing.cardholder.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.github.piotrkozuch.issuing.common.dto.BillingAddress;

import java.time.LocalDate;

import static io.github.piotrkozuch.issuing.utils.Checks.checkRequired;

@JsonDeserialize(builder = CardholderCreateRequest.Builder.class)
public class CardholderCreateRequest {

    public final String firstName;
    public final String lastName;
    public final LocalDate birthDate;
    public final String email;
    public final String phone;
    public final BillingAddress billingAddress;

    private CardholderCreateRequest(Builder builder) {
        this.firstName = checkRequired("firstName", builder.firstName);
        this.lastName = checkRequired("lastName", builder.lastName);
        this.birthDate = checkRequired("birthDate", builder.birthDate);
        this.email = checkRequired("email", builder.email);
        this.phone = checkRequired("phone", builder.phone);
        this.billingAddress = checkRequired("billingAddress", builder.billingAddress);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private String firstName;
        private String lastName;
        private LocalDate birthDate;
        private String email;
        private String phone;
        private BillingAddress billingAddress;

        private Builder() {

        }

        public static Builder cardholderCreateRequest() {
            return new Builder();
        }

        public CardholderCreateRequest build() {
            return new CardholderCreateRequest(this);
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder billingAddress(BillingAddress billingAddress) {
            this.billingAddress = billingAddress;
            return this;
        }
    }
}
