package io.github.piotrkozuch.issuing.card.service;

import io.github.piotrkozuch.issuing.common.types.CardBrand;

public interface CardSensitiveDetailsService {

    String generateCvv();

    String generatePan(CardBrand cardBrand);

    int generateExpiryYear();

    int generateExpiryMonth();
}
