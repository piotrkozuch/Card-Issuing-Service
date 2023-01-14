package io.github.piotrkozuch.issuing.card.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static io.github.piotrkozuch.issuing.utils.Checks.checkRequired;

@Entity
@Table(name = "cards_sensitive_details")
public class CardSensitiveDetails {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID cardId;

    @Column(nullable = false)
    private String cvv;

    @Column(nullable = false, unique = true)
    private String pan;

    @Column(nullable = false)
    private int expiryMonth;

    @Column(nullable = false)
    private int expiryYear;

    @Column(nullable = false)
    private String nameOnCard;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CardSensitiveDetailsState state;

    @Column(nullable = false)
    private Instant createdDate;

    @Column(nullable = false)
    private Instant updatedDate;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = checkRequired("id", id);
    }

    public UUID getCardId() {
        return cardId;
    }

    public void setCardId(UUID cardId) {
        this.cardId = checkRequired("cardId", cardId);
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = checkRequired("cvv", cvv);
    }

    public int getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(int expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public int getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(int expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = checkRequired("nameOnCard", nameOnCard);
    }

    public CardSensitiveDetailsState getState() {
        return state;
    }

    public void setState(CardSensitiveDetailsState state) {
        this.state = checkRequired("state", state);
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

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardSensitiveDetails that = (CardSensitiveDetails) o;
        return expiryMonth == that.expiryMonth && expiryYear == that.expiryYear && Objects.equals(id, that.id) && Objects.equals(cardId, that.cardId) && Objects.equals(cvv, that.cvv) && Objects.equals(pan, that.pan) && Objects.equals(nameOnCard, that.nameOnCard) && state == that.state && Objects.equals(createdDate, that.createdDate) && Objects.equals(updatedDate, that.updatedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cardId, cvv, pan, expiryMonth, expiryYear, nameOnCard, state, createdDate, updatedDate);
    }
}
