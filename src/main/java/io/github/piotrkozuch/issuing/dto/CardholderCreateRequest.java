package io.github.piotrkozuch.issuing.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.github.piotrkozuch.issuing.types.BillingAddress;

import java.time.LocalDate;

import static io.github.piotrkozuch.issuing.utils.Checks.checkRequired;

public class CardholderCreateRequest {

    public final String firstName;
    public final String lastName;
    public final LocalDate birthDate;
    public final String email;
    public final String phone;
    public final BillingAddress billingAddress;

    @JsonCreator
    public CardholderCreateRequest(String firstName,
                                   String lastName,
                                   LocalDate birthDate,
                                   String email,
                                   String phone,
                                   BillingAddress billingAddress) {
        this.firstName = checkRequired("firstName", firstName);
        this.lastName = checkRequired("lastName", lastName);
        this.birthDate = checkRequired("birthDate", birthDate);
        this.email = checkRequired("email", email);
        this.phone = checkRequired("phone", phone);
        this.billingAddress = checkRequired("billingAddress", billingAddress);
    }
}
