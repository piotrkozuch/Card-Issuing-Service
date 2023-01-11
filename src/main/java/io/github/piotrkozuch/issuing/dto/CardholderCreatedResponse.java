package io.github.piotrkozuch.issuing.dto;

import io.github.piotrkozuch.issuing.types.BillingAddress;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static io.github.piotrkozuch.issuing.utils.Checks.checkRequired;

public class CardholderCreatedResponse {

    public final UUID id;
    public final String firstName;
    public final String lastName;
    public final LocalDate birthDate;
    public final String email;
    public final String phone;
    public final BillingAddress billingAddress;
    public final Instant createdDate;
    public final Instant updatedDate;

    private CardholderCreatedResponse(Builder builder) {
        id = checkRequired("id", builder.id);
        firstName = checkRequired("firstName", builder.firstName);
        lastName = checkRequired("lastName", builder.lastName);
        birthDate = checkRequired("birthDate", builder.birthDate);
        email = checkRequired("email", builder.email);
        phone = checkRequired("phone", builder.phone);
        createdDate = checkRequired("createdDate", builder.createdDate);
        updatedDate = checkRequired("updatedDate", builder.updatedDate);
        billingAddress = checkRequired("billingAddress", builder.billingAddress);
    }

    public static class Builder {

        private UUID id;
        private String firstName;
        private String lastName;
        private LocalDate birthDate;
        private String email;
        private String phone;
        private BillingAddress billingAddress;
        private Instant createdDate;
        private Instant updatedDate;

        private Builder() {

        }

        public CardholderCreatedResponse build() {
            return new CardholderCreatedResponse(this);
        }

        public static Builder cardholderCreatedResponse() {
            return new Builder();
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
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

        public Builder createdDate(Instant createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder updatedDate(Instant updatedDate) {
            this.updatedDate = updatedDate;
            return this;
        }
    }
}
