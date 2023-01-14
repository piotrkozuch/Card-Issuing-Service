package io.github.piotrkozuch.issuing.card.dto;

import io.github.piotrkozuch.issuing.common.types.CardBrand;
import io.github.piotrkozuch.issuing.common.types.CardType;
import io.github.piotrkozuch.issuing.common.types.Currency;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static io.github.piotrkozuch.issuing.card.dto.CardResponse.Builder.cardResponse;
import static io.github.piotrkozuch.issuing.utils.Checks.checkRequired;

public class CardResponse {

    public final UUID id;
    public final UUID cardholderId;
    public final String state;
    public final String maskedPan;
    public final UUID token;
    public final CardBrand brand;
    public final CardType type;
    public final Currency currency;
    public final Instant createdDate;
    public final Instant updatedDate;
    public final Optional<CardSensitiveDetailsResponse> cardSensitiveDetails;

    public CardResponse(UUID id,
                        UUID cardholderId,
                        String state,
                        String maskedPan,
                        UUID token,
                        CardBrand brand,
                        CardType type,
                        Currency currency,
                        Instant createdDate,
                        Instant updatedDate,
                        CardSensitiveDetailsResponse cardSensitiveDetails) {
        this(cardResponse()
            .id(id)
            .cardholderId(cardholderId)
            .state(state)
            .maskedPan(maskedPan)
            .token(token)
            .brand(brand)
            .type(type)
            .currency(currency)
            .createdDate(createdDate)
            .updatedDate(updatedDate)
            .cardSensitiveDetails(cardSensitiveDetails)
        );
    }

    public CardResponse(Builder builder) {
        this.id = checkRequired("id", builder.id);
        this.cardholderId = checkRequired("cardholderId", builder.cardholderId);
        this.state = checkRequired("state", builder.state);
        this.maskedPan = checkRequired("maskedPan", builder.maskedPan);
        this.token = checkRequired("token", builder.token);
        this.brand = checkRequired("brand", builder.brand);
        this.type = checkRequired("type", builder.type);
        this.currency = checkRequired("currency", builder.currency);
        this.createdDate = checkRequired("createdDate", builder.createdDate);
        this.updatedDate = checkRequired("updatedDate", builder.updatedDate);
        this.cardSensitiveDetails = Optional.ofNullable(builder.cardSensitiveDetails);
    }

    public static class Builder {

        private UUID id;
        private UUID cardholderId;
        private String state;
        private String maskedPan;
        private UUID token;
        private CardBrand brand;
        private CardType type;
        private Currency currency;
        private Instant createdDate;
        private Instant updatedDate;
        private CardSensitiveDetailsResponse cardSensitiveDetails;

        private Builder() {

        }

        public static Builder cardResponse() {
            return new Builder();
        }

        public CardResponse build() {
            return new CardResponse(this);
        }

        public Builder cardSensitiveDetails(CardSensitiveDetailsResponse cardSensitiveDetails) {
            this.cardSensitiveDetails = cardSensitiveDetails;
            return this;
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder cardholderId(UUID cardholderId) {
            this.cardholderId = cardholderId;
            return this;
        }

        public Builder state(String state) {
            this.state = state;
            return this;
        }

        public Builder maskedPan(String maskedPan) {
            this.maskedPan = maskedPan;
            return this;
        }

        public Builder token(UUID token) {
            this.token = token;
            return this;
        }

        public Builder brand(CardBrand brand) {
            this.brand = brand;
            return this;
        }

        public Builder type(CardType type) {
            this.type = type;
            return this;
        }

        public Builder currency(Currency currency) {
            this.currency = currency;
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
