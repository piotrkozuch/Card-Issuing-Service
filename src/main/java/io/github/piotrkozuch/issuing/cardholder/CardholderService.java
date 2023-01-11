package io.github.piotrkozuch.issuing.cardholder;

import io.github.piotrkozuch.issuing.model.Cardholder;
import io.github.piotrkozuch.issuing.types.BillingAddress;

import java.time.LocalDate;

public interface CardholderService {

    Cardholder createCardholder(String firstName,
                                String lastName,
                                LocalDate birthDate,
                                String email,
                                String phone,
                                BillingAddress billingAddress);
}
