package io.github.piotrkozuch.issuing.cardholder;

import io.github.piotrkozuch.issuing.model.Cardholder;
import io.github.piotrkozuch.issuing.types.BillingAddress;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CardholderService {

    Cardholder activateCardholder(UUID id);

    Cardholder createCardholder(String firstName,
                                String lastName,
                                LocalDate birthDate,
                                String email,
                                String phone,
                                BillingAddress billingAddress);

    Cardholder getCardholder(UUID id);

    List<Cardholder> getAllCardholders();

    void deleteCardholder(UUID id);
}
