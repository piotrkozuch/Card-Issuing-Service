package io.github.piotrkozuch.issuing.cardholder.service;

import io.github.piotrkozuch.issuing.cardholder.model.Cardholder;
import io.github.piotrkozuch.issuing.common.dto.BillingAddress;

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
