package io.github.piotrkozuch.issuing.cardholder;

import io.github.piotrkozuch.issuing.dto.CardholderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import static io.github.piotrkozuch.issuing.model.CardholderState.ACTIVE;
import static io.github.piotrkozuch.issuing.model.CardholderState.PENDING;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CardholderControllerTest implements CardholderTestData {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CardholderRepository cardholderRepository;

    @BeforeEach
    void setup() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        cardholderRepository.deleteAll();
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
        var body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.firstName).isEqualTo(request.firstName);
        assertThat(body.lastName).isEqualTo(request.lastName);
        assertThat(body.birthDate).isEqualTo(request.birthDate);
        assertThat(body.email).isEqualTo(request.email);
        assertThat(body.phone).isEqualTo(request.phone);
        assertThat(body.state).isEqualTo(PENDING.name());
        assertThat(body.createdDate).isNotNull();
        assertThat(body.updatedDate).isNotNull();
        assertThat(body.billingAddress.streetLine1).isEqualTo(request.billingAddress.streetLine1);
        assertThat(body.billingAddress.streetLine2).isEqualTo(request.billingAddress.streetLine2);
        assertThat(body.billingAddress.city).isEqualTo(request.billingAddress.city);
        assertThat(body.billingAddress.country).isEqualTo(request.billingAddress.country);
        assertThat(body.billingAddress.postcode).isEqualTo(request.billingAddress.postcode);
        assertThat(body.id).isNotNull();

        assertThat(cardholderRepository.findById(body.id)).isPresent();
    }

    @Test
    void should_activate_cardholder() {
        // given
        var cardholder = cardholderRepository.save(createCardholder());
        var url = urlFor("/api/v0.1/cardholders/activate/" + cardholder.getId());

        // when
        var response = restTemplate.patchForObject(url, null, CardholderResponse.class);

        // then
        var updatedCardholder = cardholderRepository.findById(response.id).get();
        assertThat(updatedCardholder.getState()).isEqualTo(ACTIVE);
    }

    private String urlFor(String path) {
        return "http://localhost:" + port + path;
    }

}
