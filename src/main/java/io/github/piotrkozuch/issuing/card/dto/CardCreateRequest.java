package io.github.piotrkozuch.issuing.card.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.neovisionaries.i18n.CurrencyCode;
import io.github.piotrkozuch.issuing.common.types.CardBrand;
import io.github.piotrkozuch.issuing.common.types.CardType;

import java.util.UUID;

import static io.github.piotrkozuch.issuing.utils.Checks.checkRequired;

@JsonDeserialize(builder = CardCreateRequest.Builder.class)
public class CardCreateRequest {

    public final UUID cardholderId;
    public final CurrencyCode currency;
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
        private CurrencyCode currency;
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

        public Builder currency(CurrencyCode currency) {
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
