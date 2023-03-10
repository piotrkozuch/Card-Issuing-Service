package io.github.piotrkozuch.issuing.card;

import io.github.piotrkozuch.issuing.card.dto.CardResponse;
import io.github.piotrkozuch.issuing.card.repository.CardJpaRepository;
import io.github.piotrkozuch.issuing.card.repository.CardSensitiveDetailsJpaRepository;
import io.github.piotrkozuch.issuing.cardholder.CardholderTestData;
import io.github.piotrkozuch.issuing.cardholder.repository.CardholderJpaRepository;
import io.github.piotrkozuch.issuing.common.types.CardBrand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.time.LocalDate;

import static io.github.piotrkozuch.issuing.card.model.CardState.INACTIVE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CardControllerTest implements CardholderTestData, CardTestData {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CardJpaRepository cardJpaRepository;

    @Autowired
    private CardholderJpaRepository cardholderJpaRepository;

    @Autowired
    private CardSensitiveDetailsJpaRepository cardSensitiveDetailsJpaRepository;

    @BeforeEach
    void setup() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        cardholderJpaRepository.deleteAll();
        cardJpaRepository.deleteAll();
        cardSensitiveDetailsJpaRepository.deleteAll();
    }

    @ParameterizedTest
    @CsvSource({
        "VISA",
        "MASTERCARD",
        "MAESTRO"
    })
    void should_issue_new_card(CardBrand cardBrand) {
        // given
        var url = urlFor("/api/v0.1/cards");
        var cardholder = cardholderJpaRepository.save(createCardholder().activate());
        var request = aCardCreateRequest()
            .cardBrand(cardBrand)
            .cardholderId(cardholder.getId())
            .build();

        // when
        var response = restTemplate.postForEntity(url, request, CardResponse.class);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        var cardResponse = response.getBody();

        assertThat(cardResponse.cardSensitiveDetails).isEmpty();
        assertThat(cardResponse.cardholderId).isEqualTo(request.cardholderId);
        assertThat(cardResponse.currency).isEqualTo(request.currency);
        assertThat(cardResponse.brand).isEqualTo(request.cardBrand);
        assertThat(cardResponse.type).isEqualTo(request.cardType);

        var newCard = cardJpaRepository.findById(cardResponse.id).get();
        assertThat(cardResponse.maskedPan).isEqualTo(newCard.getMaskedPan());

        assertThat(newCard.getState()).isEqualTo(INACTIVE);
        assertThat(newCard.getBrand()).isEqualTo(request.cardBrand);
        assertThat(newCard.getType()).isEqualTo(request.cardType);
        assertThat(newCard.getCurrency()).isEqualTo(request.currency);
        assertThat(newCard.getCardholderId()).isEqualTo(request.cardholderId);
        assertThat(newCard.getId()).isEqualTo(cardResponse.id);

        var cardSensitiveDetails = newCard.getCardSensitiveDetails();
        assertThat(cardSensitiveDetails.getCvv()).isNotEmpty();
        assertThat(cardSensitiveDetails.getExpiryMonth()).isEqualTo(LocalDate.now().getMonthValue());
        assertThat(cardSensitiveDetails.getExpiryYear()).isEqualTo(LocalDate.now().getYear() + 3);
        assertThat(cardSensitiveDetails.getPan()).isNotEmpty();
        assertThat(cardSensitiveDetails.getNameOnCard()).isEqualTo(cardholder.getLegalName());
    }

    @Test
    void should_return_card_by_id() {
        // given
        var cardholder = cardholderJpaRepository.save(createCardholder().activate());
        var card = createCard();
        var details = createCardSensitiveDetails();
        cardSensitiveDetailsJpaRepository.save(details);
        card.setCardSensitiveDetails(details);
        card.setCardholderId(cardholder.getId());
        cardJpaRepository.save(card);

        var url = urlFor("/api/v0.1/cards/" + card.getId());

        // when
        var response = restTemplate.getForEntity(url, CardResponse.class);

        // then
        var cardResponse = response.getBody();

        assertThat(cardResponse.cardSensitiveDetails).isEmpty();
        assertThat(cardResponse.cardholderId).isEqualTo(card.getCardholderId());
        assertThat(cardResponse.currency).isEqualTo(card.getCurrency());
        assertThat(cardResponse.brand).isEqualTo(card.getBrand());
        assertThat(cardResponse.type).isEqualTo(card.getType());
    }

    @Test
    void should_return_card_with_details() {
        // given
        var cardholder = cardholderJpaRepository.save(createCardholder().activate());
        var card = createCard();
        card.setCardholderId(cardholder.getId());

        var cardDetails = createCardSensitiveDetails();
        cardDetails.setNameOnCard(cardholder.getLegalName());
        cardSensitiveDetailsJpaRepository.save(cardDetails);

        card.setCardSensitiveDetails(cardDetails);
        cardJpaRepository.save(card);

        var url = urlFor("/api/v0.1/cards/" + card.getId() + "/details");

        // when
        var response = restTemplate.getForEntity(url, CardResponse.class);

        // then
        var cardResponse = response.getBody();

        var detailsResponse = cardResponse.cardSensitiveDetails.get();
        assertThat(detailsResponse.cvv).isEqualTo(cardDetails.getCvv());
        assertThat(detailsResponse.pan).isEqualTo(cardDetails.getPan());
        assertThat(detailsResponse.expiryMonth).isEqualTo(cardDetails.getExpiryMonth());
        assertThat(detailsResponse.expiryYear).isEqualTo(cardDetails.getExpiryYear());
        assertThat(detailsResponse.nameOnCard).isEqualTo(cardholder.getLegalName());

        assertThat(cardResponse.cardholderId).isEqualTo(card.getCardholderId());
        assertThat(cardResponse.currency).isEqualTo(card.getCurrency());
        assertThat(cardResponse.brand).isEqualTo(card.getBrand());
        assertThat(cardResponse.type).isEqualTo(card.getType());
    }

    private String urlFor(String path) {
        return "http://localhost:" + port + path;
    }
}