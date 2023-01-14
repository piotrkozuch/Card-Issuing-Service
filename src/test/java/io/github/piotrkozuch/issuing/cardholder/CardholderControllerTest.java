package io.github.piotrkozuch.issuing.cardholder;

import io.github.piotrkozuch.issuing.cardholder.repository.CardholderJpaRepository;
import io.github.piotrkozuch.issuing.dto.CardholderResponse;
import io.github.piotrkozuch.issuing.model.cardholder.Cardholder;
import io.github.piotrkozuch.issuing.model.cardholder.CardholderState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.stream.Stream;

import static io.github.piotrkozuch.issuing.model.cardholder.CardholderState.ACTIVE;
import static io.github.piotrkozuch.issuing.model.cardholder.CardholderState.DELETED;
import static io.github.piotrkozuch.issuing.model.cardholder.CardholderState.PENDING;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CardholderControllerTest implements CardholderTestData {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CardholderJpaRepository cardholderJpaRepository;

    @BeforeEach
    void setup() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        cardholderJpaRepository.deleteAll();
    }

    @Test
    void should_create_new_cardholder() {
        // given
        var url = urlFor("/api/v0.1/cardholders");
        var request = aCardholderCreateRequest().build();

        // when
        var response = restTemplate.postForEntity(url, request, CardholderResponse.class);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        var newCardholder = response.getBody();

        assertThat(newCardholder).isNotNull();
        assertThat(newCardholder.firstName).isEqualTo(request.firstName);
        assertThat(newCardholder.lastName).isEqualTo(request.lastName);
        assertThat(newCardholder.birthDate).isEqualTo(request.birthDate);
        assertThat(newCardholder.email).isEqualTo(request.email);
        assertThat(newCardholder.phone).isEqualTo(request.phone);
        assertThat(newCardholder.state).isEqualTo(PENDING.name());
        assertThat(newCardholder.createdDate).isNotNull();
        assertThat(newCardholder.updatedDate).isNotNull();
        assertThat(newCardholder.billingAddress.streetLine1).isEqualTo(request.billingAddress.streetLine1);
        assertThat(newCardholder.billingAddress.streetLine2).isEqualTo(request.billingAddress.streetLine2);
        assertThat(newCardholder.billingAddress.city).isEqualTo(request.billingAddress.city);
        assertThat(newCardholder.billingAddress.country).isEqualTo(request.billingAddress.country);
        assertThat(newCardholder.billingAddress.postcode).isEqualTo(request.billingAddress.postcode);
        assertThat(newCardholder.id).isNotNull();

        assertThat(cardholderJpaRepository.findById(newCardholder.id)).isPresent();
    }

    @Test
    void should_activate_cardholder() {
        // given
        var cardholder = cardholderJpaRepository.save(createCardholder());
        var url = urlFor("/api/v0.1/cardholders/activate/" + cardholder.getId());

        // when
        var activeCardholder = restTemplate.patchForObject(url, null, CardholderResponse.class);

        // then
        assertCardholder(activeCardholder, cardholder, ACTIVE);

        var updatedCardholder = cardholderJpaRepository.findById(activeCardholder.id).get();
        assertThat(updatedCardholder.getState()).isEqualTo(ACTIVE);
    }

    @Test
    void should_get_cardholder_by_id() {
        // given
        var cardholder = cardholderJpaRepository.save(createCardholder());
        var url = urlFor("/api/v0.1/cardholders/" + cardholder.getId());

        // when
        var response = restTemplate.getForEntity(url, CardholderResponse.class);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        var cardholderResponse = response.getBody();

        assertCardholder(cardholderResponse, cardholder, PENDING);
    }

    @Test
    void should_delete_cardholder() {
        // given
        var cardholder = cardholderJpaRepository.save(createCardholder());
        var url = urlFor("/api/v0.1/cardholders/" + cardholder.getId());

        // when
        restTemplate.delete(url);

        // then
        var deletedCardholder = cardholderJpaRepository.findById(cardholder.getId()).get();
        assertThat(deletedCardholder.getState()).isEqualTo(DELETED);
    }

    @Test
    void should_get_all_cardholders() {
        // given
        var cardholder1 = cardholderJpaRepository.save(createCardholder());
        var cardholder2 = cardholderJpaRepository.save(createCardholder());
        var cardholder3 = cardholderJpaRepository.save(createCardholder().delete());
        var url = urlFor("/api/v0.1/cardholders");

        // when
        var response = restTemplate.getForEntity(url, CardholderResponse[].class);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode().value()).isEqualTo(200);

        var cardholders = response.getBody();
        assertThat(cardholders.length).isEqualTo(2);

        assertThat(Stream.of(cardholders).map(c -> c.id).anyMatch(id -> cardholder1.getId().equals(id))).isTrue();
        assertThat(Stream.of(cardholders).map(c -> c.id).anyMatch(id -> cardholder2.getId().equals(id))).isTrue();
        assertThat(Stream.of(cardholders).map(c -> c.id).anyMatch(id -> cardholder3.getId().equals(id))).isFalse();
    }

    private String urlFor(String path) {
        return "http://localhost:" + port + path;
    }

    private void assertCardholder(CardholderResponse cardholderResponse, Cardholder cardholder, CardholderState state) {
        assertThat(cardholderResponse).isNotNull();
        assertThat(cardholderResponse.firstName).isEqualTo(cardholder.getFirstName());
        assertThat(cardholderResponse.lastName).isEqualTo(cardholder.getLastName());
        assertThat(cardholderResponse.birthDate).isEqualTo(cardholder.getBirthDate());
        assertThat(cardholderResponse.email).isEqualTo(cardholder.getEmail());
        assertThat(cardholderResponse.phone).isEqualTo(cardholder.getPhone());
        assertThat(cardholderResponse.state).isEqualTo(state.name());
        assertThat(cardholderResponse.createdDate).isNotNull();
        assertThat(cardholderResponse.updatedDate).isNotNull();
        assertThat(cardholderResponse.billingAddress.streetLine1).isEqualTo(cardholder.getAddress().getStreetLine1());
        assertThat(cardholderResponse.billingAddress.streetLine2.get()).isEqualTo(cardholder.getAddress().getStreetLine2());
        assertThat(cardholderResponse.billingAddress.city).isEqualTo(cardholder.getAddress().getCity());
        assertThat(cardholderResponse.billingAddress.country).isEqualTo(cardholder.getAddress().getCountry());
        assertThat(cardholderResponse.billingAddress.postcode).isEqualTo(cardholder.getAddress().getPostcode());
        assertThat(cardholderResponse.id).isEqualTo(cardholder.getId());
    }

}
