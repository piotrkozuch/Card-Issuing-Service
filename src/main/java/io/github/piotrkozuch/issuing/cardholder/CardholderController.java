package io.github.piotrkozuch.issuing.cardholder;

import io.github.piotrkozuch.issuing.dto.CardholderCreateRequest;
import io.github.piotrkozuch.issuing.dto.CardholderResponse;
import io.github.piotrkozuch.issuing.model.Cardholder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static io.github.piotrkozuch.issuing.dto.CardholderResponse.Builder.cardholderResponse;
import static io.github.piotrkozuch.issuing.types.BillingAddress.Builder.billingAddress;
import static io.github.piotrkozuch.issuing.utils.Checks.checkRequired;

@RestController
@RequestMapping("/cardholders")
public class CardholderController {

    private final CardholderService cardholderService;

    @Autowired
    public CardholderController(CardholderService cardholderService) {
        this.cardholderService = checkRequired("cardholderService", cardholderService);
    }

    @PostMapping
    @ResponseBody
    public CardholderResponse createCardholder(@RequestBody CardholderCreateRequest request) {
        final var cardholder = cardholderService.createCardholder(request.firstName,
            request.lastName,
            request.birthDate,
            request.email,
            request.phone,
            request.billingAddress);

        return createCardholderResponse(cardholder);
    }

    @PatchMapping("/activate/{id}")
    @ResponseBody
    public CardholderResponse activateCardholder(@PathVariable UUID id) {
        final var cardholder = cardholderService.activateCardholder(id);

        return createCardholderResponse(cardholder);
    }

    private CardholderResponse createCardholderResponse(Cardholder cardholder) {
        final var billingAddress = billingAddress()
            .streetLine1(cardholder.getAddress().getStreetLine1())
            .streetLine2(cardholder.getAddress().getStreetLine2())
            .city(cardholder.getAddress().getCity())
            .country(cardholder.getAddress().getCountry())
            .postcode(cardholder.getAddress().getPostcode())
            .build();

        return cardholderResponse()
            .id(cardholder.getId())
            .createdDate(cardholder.getCreatedDate())
            .updatedDate(cardholder.getUpdatedDate())
            .lastName(cardholder.getLastName())
            .firstName(cardholder.getFirstName())
            .phone(cardholder.getPhone())
            .email(cardholder.getPhone())
            .birthDate(cardholder.getBirthDate())
            .email(cardholder.getEmail())
            .phone(cardholder.getPhone())
            .billingAddress(billingAddress)
            .state(cardholder.getState().name())
            .build();
    }

}
