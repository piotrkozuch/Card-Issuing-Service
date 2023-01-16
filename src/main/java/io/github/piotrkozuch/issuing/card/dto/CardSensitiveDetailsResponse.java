package io.github.piotrkozuch.issuing.card.dto;

import java.time.Instant;
import java.util.UUID;

import static io.github.piotrkozuch.issuing.card.dto.CardSensitiveDetailsResponse.Builder.cardSensitiveDetailsResponse;
import static io.github.piotrkozuch.issuing.utils.Checks.checkRequired;

public class CardSensitiveDetailsResponse {

    public final UUID id;
    public final String pan;
    public final String cvv;
    public final int expiryMonth;
    public final int expiryYear;
    public final String nameOnCard;
    public final Instant createdDate;
    public final Instant updatedDate;

    public CardSensitiveDetailsResponse(UUID id,
                                        String pan,
                                        String cvv,
                                        Integer expiryMonth,
                                        Integer expiryYear,
                                        String nameOnCard,
                                        Instant createdDate,
                                        Instant updatedDate) {
        this(cardSensitiveDetailsResponse()
            .id(id)
            .pan(pan)
            .cvv(cvv)
            .expiryMonth(expiryMonth)
            .expiryYear(expiryYear)
            .nameOnCard(nameOnCard)
            .createdDate(createdDate)
            .updatedDate(updatedDate));
    }

    public CardSensitiveDetailsResponse(Builder builder) {
        this.id = checkRequired("id", builder.id);
        this.pan = checkRequired("pan", builder.pan);
        this.cvv = checkRequired("cvv", builder.cvv);
        this.expiryMonth = checkRequired("expiryMonth", builder.expiryMonth);
        this.expiryYear = checkRequired("expiryYear", builder.expiryYear);
        this.nameOnCard = checkRequired("nameOnCard", builder.nameOnCard);
        this.createdDate = checkRequired("createdDate", builder.createdDate);
        this.updatedDate = checkRequired("updatedDate", builder.updatedDate);
    }

    public static class Builder {

        private UUID id;
        private String cvv;
        private String pan;
        private int expiryMonth;
        private int expiryYear;
        private String nameOnCard;
        private Instant createdDate;
        private Instant updatedDate;

        private Builder() {

        }

        public CardSensitiveDetailsResponse build() {
            return new CardSensitiveDetailsResponse(this);
        }

        public static Builder cardSensitiveDetailsResponse() {
            return new Builder();
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder pan(String pan) {
            this.pan = pan;
            return this;
        }

        public Builder cvv(String cvv) {
            this.cvv = cvv;
            return this;
        }

        public Builder expiryMonth(int expiryMonth) {
            this.expiryMonth = expiryMonth;
            return this;
        }

        public Builder expiryYear(int expiryYear) {
            this.expiryYear = expiryYear;
            return this;
        }

        public Builder nameOnCard(String nameOnCard) {
            this.nameOnCard = nameOnCard;
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
