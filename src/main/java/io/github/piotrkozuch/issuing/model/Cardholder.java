package io.github.piotrkozuch.issuing.model;

import io.github.piotrkozuch.issuing.cardholder.exception.CardholderChangeStateException;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import static io.github.piotrkozuch.issuing.model.CardholderState.ACTIVE;
import static io.github.piotrkozuch.issuing.model.CardholderState.PENDING;
import static io.github.piotrkozuch.issuing.utils.Checks.checkRequired;

@Entity
@Table(name = "cardholders")
public class Cardholder {

    @Id
    private UUID id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private LocalDate birthDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardholderState state;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private Instant createdDate;
    @Column(nullable = false)
    private Instant updatedDate;

    @Embedded
    private Address address;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = checkRequired("id", id);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = checkRequired("firstName", firstName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = checkRequired("lastName", lastName);
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = checkRequired("birthDate", birthDate);
    }

    public CardholderState getState() {
        return state;
    }

    public void setState(CardholderState state) {
        this.state = checkRequired("state", state);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = checkRequired("phone", phone);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = checkRequired("email", email);
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = checkRequired("address", address);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cardholder that = (Cardholder) o;
        return Objects.equals(id, that.id) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(birthDate, that.birthDate) && Objects.equals(state, that.state) && Objects.equals(phone, that.phone) && Objects.equals(email, that.email) && Objects.equals(createdDate, that.createdDate) && Objects.equals(updatedDate, that.updatedDate) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, birthDate, state, phone, email, createdDate, updatedDate, address);
    }

    public Cardholder activate() {
        if (hasState(PENDING)) {
            setState(ACTIVE);
            return this;
        }

        throw new CardholderChangeStateException(id, getState(), PENDING);
    }

    public boolean hasState(CardholderState state) {
        return getState() == checkRequired("state", state);
    }
}
