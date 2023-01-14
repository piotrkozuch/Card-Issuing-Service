package io.github.piotrkozuch.issuing.card.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.github.piotrkozuch.issuing.cardholder.dto.CardholderCreateRequest;
import io.github.piotrkozuch.issuing.common.types.CardBrand;
import io.github.piotrkozuch.issuing.common.types.CardType;
import io.github.piotrkozuch.issuing.common.types.Currency;

import java.util.UUID;

import static io.github.piotrkozuch.issuing.utils.Checks.checkRequired;

@JsonDeserialize(builder = CardholderCreateRequest.Builder.class)
public class CardCreateRequest {

    public final UUID cardholderId;
    public final Currency currency;
    public final CardBrand cardBrand;
    public final CardType cardType;

    private CardCreateRequest(Builder builder) {
        this.cardholderId = checkRequired("cardholderId", builder.cardholderId);
        this.currency = checkRequired("currency", builder.currency);
        this.cardBrand = checkRequired("cardBrand", builder.cardBrand);
        this.cardType = checkRequired("cardType", builder.cardType);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private UUID cardholderId;
        private Currency currency;
        private CardBrand cardBrand;
        private CardType cardType;

        private Builder() {

        }

        public static Builder cardCreateRequest() {
            return new Builder();
        }

        public CardCreateRequest build() {
            return new CardCreateRequest(this);
        }

        public Builder cardholderId(UUID cardholderId) {
            this.cardholderId = cardholderId;
            return this;
        }

        public Builder currency(Currency currency) {
            this.currency = currency;
            return this;
        }

        public Builder cardBrand(CardBrand cardBrand) {
            this.cardBrand = cardBrand;
            return this;
        }

        public Builder cardType(CardType cardType) {
            this.cardType = cardType;
            return this;
        }
    }
}
