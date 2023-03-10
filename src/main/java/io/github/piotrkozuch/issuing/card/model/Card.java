package io.github.piotrkozuch.issuing.card.model;

import com.neovisionaries.i18n.CurrencyCode;
import io.github.piotrkozuch.issuing.common.types.CardBrand;
import io.github.piotrkozuch.issuing.common.types.CardType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static io.github.piotrkozuch.issuing.utils.Checks.checkRequired;

@Entity
@Table(name = "cards")
public class Card {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID cardholderId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CardState state;

    @Column(nullable = false)
    private String maskedPan;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CardBrand brand;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CardType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CurrencyCode currency;

    @Column(nullable = false)
    private Instant createdDate;

    @Column(nullable = false)
    private Instant updatedDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "token", referencedColumnName = "id")
    private CardSensitiveDetails cardSensitiveDetails;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = checkRequired("id", id);
    }

    public UUID getCardholderId() {
        return cardholderId;
    }

    public void setCardholderId(UUID cardholderId) {
        this.cardholderId = checkRequired("cardholderId", cardholderId);
    }

    public CardState getState() {
        return state;
    }

    public void setState(CardState state) {
        this.state = checkRequired("state", state);
    }

    public String getMaskedPan() {
        return maskedPan;
    }

    public void setMaskedPan(String maskedPan) {
        this.maskedPan = checkRequired("maskedPan", maskedPan);
    }

    public CardBrand getBrand() {
        return brand;
    }

    public void setBrand(CardBrand brand) {
        this.brand = checkRequired("brand", brand);
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = checkRequired("type", type);
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = checkRequired("createdDate", createdDate);
    }

    public Instant getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = checkRequired("updatedDate", updatedDate);
    }

    public CurrencyCode getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyCode currency) {
        this.currency = currency;
    }

    public CardSensitiveDetails getCardSensitiveDetails() {
        return cardSensitiveDetails;
    }

    public void setCardSensitiveDetails(CardSensitiveDetails cardSensitiveDetails) {
        this.cardSensitiveDetails = cardSensitiveDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(id, card.id) && Objects.equals(cardholderId, card.cardholderId) && state == card.state && Objects.equals(maskedPan, card.maskedPan) && brand == card.brand && type == card.type && currency == card.currency && Objects.equals(createdDate, card.createdDate) && Objects.equals(updatedDate, card.updatedDate) && Objects.equals(cardSensitiveDetails, card.cardSensitiveDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cardholderId, state, maskedPan, brand, type, currency, createdDate, updatedDate, cardSensitiveDetails);
    }
}
