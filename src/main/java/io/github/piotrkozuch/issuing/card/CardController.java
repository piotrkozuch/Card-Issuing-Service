package io.github.piotrkozuch.issuing.card;

import io.github.piotrkozuch.issuing.card.dto.CardCreateRequest;
import io.github.piotrkozuch.issuing.card.dto.CardResponse;
import io.github.piotrkozuch.issuing.card.dto.CardSensitiveDetailsResponse;
import io.github.piotrkozuch.issuing.card.model.Card;
import io.github.piotrkozuch.issuing.card.model.CardSensitiveDetails;
import io.github.piotrkozuch.issuing.card.service.CardManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.github.piotrkozuch.issuing.card.dto.CardResponse.Builder.cardResponse;
import static io.github.piotrkozuch.issuing.card.dto.CardSensitiveDetailsResponse.Builder.cardSensitiveDetailsResponse;
import static io.github.piotrkozuch.issuing.utils.Checks.checkRequired;

@RestController
@RequestMapping("/cards")
public class CardController {

    private final CardManager cardManager;

    @Autowired
    public CardController(CardManager cardManager) {
        this.cardManager = checkRequired("cardManager", cardManager);
    }

    @ResponseBody
    @PostMapping
    public CardResponse issueNewCard(@RequestBody CardCreateRequest request) {
        final var card = cardManager.issueNewCard(request.cardholderId, request.cardBrand, request.cardType, request.currency);
        return createCardResponse(card, false);
    }

    @ResponseBody
    @GetMapping("/{id}")
    public CardResponse getCard(@PathVariable UUID id) {
        final var card = cardManager.get(id);
        return createCardResponse(card, false);
    }

    @ResponseBody
    @GetMapping("/{id}/details")
    public CardResponse getCardWithSensitiveDetails(@PathVariable UUID id) {
        final var card = cardManager.get(id);
        return createCardResponse(card, true);
    }

    @ResponseBody
    @GetMapping
    public List<CardResponse> getAllCards() {
        return cardManager.findAll().stream()
            .map(card -> createCardResponse(card, false))
            .collect(Collectors.toList());
    }

    private CardResponse createCardResponse(Card card, boolean includeDetails) {
        final var builder = cardResponse()
            .id(card.getId())
            .type(card.getType())
            .currency(card.getCurrency())
            .brand(card.getBrand())
            .createdDate(card.getCreatedDate())
            .updatedDate(card.getUpdatedDate())
            .maskedPan(card.getMaskedPan())
            .cardholderId(card.getCardholderId())
            .state(card.getState().name());

        Optional.ofNullable(card.getCardSensitiveDetails())
            .filter(__ -> includeDetails)
            .map(this::createCardSensitiveDetailsResponse)
            .ifPresent(builder::cardSensitiveDetails);

        return builder.build();
    }

    private CardSensitiveDetailsResponse createCardSensitiveDetailsResponse(CardSensitiveDetails cardSensitiveDetails) {
        return cardSensitiveDetailsResponse()
            .id(cardSensitiveDetails.getId())
            .nameOnCard(cardSensitiveDetails.getNameOnCard())
            .expiryYear(cardSensitiveDetails.getExpiryYear())
            .expiryMonth(cardSensitiveDetails.getExpiryMonth())
            .pan(cardSensitiveDetails.getPan())
            .cvv(cardSensitiveDetails.getCvv())
            .createdDate(cardSensitiveDetails.getCreatedDate())
            .updatedDate(cardSensitiveDetails.getUpdatedDate())
            .build();
    }
}
