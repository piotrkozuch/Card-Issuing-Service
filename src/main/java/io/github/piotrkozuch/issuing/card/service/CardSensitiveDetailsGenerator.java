package io.github.piotrkozuch.issuing.card.service;

import io.github.piotrkozuch.issuing.card.action.exception.CardBrandNotSupportedException;
import io.github.piotrkozuch.issuing.common.types.CardBrand;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;

import static io.github.piotrkozuch.issuing.common.types.CardBrand.MAESTRO;
import static io.github.piotrkozuch.issuing.common.types.CardBrand.MASTERCARD;
import static io.github.piotrkozuch.issuing.common.types.CardBrand.VISA;
import static java.lang.String.format;

@Service
public class CardSensitiveDetailsGenerator implements CardSensitiveDetailsService {

    @Override
    public String generateCvv() {
        final var random = new Random();
        return format("%03d", random.nextInt(1000));
    }

    @Override
    public String generatePan(CardBrand cardBrand) {
        final var pan = new char[16];
        if (cardBrand == VISA) {
            pan[0] = '4';
            return genRandomPan(1, pan);
        } else if (cardBrand == MASTERCARD) {
            pan[0] = '5';
            pan[1] = '1';
            return genRandomPan(2, pan);
        } else if (cardBrand == MAESTRO) {
            pan[0] = '5';
            pan[1] = '5';
            return genRandomPan(2, pan);
        }

        throw new CardBrandNotSupportedException(cardBrand);
    }

    private String genRandomPan(int startFrom, char[] pan) {
        final var random = new Random();

        for (var i = startFrom; i < pan.length; i++) {
            var nextNumber = Character.forDigit(random.nextInt(0, 9), 10);
            pan[i] = nextNumber;
        }

        return String.valueOf(pan);
    }

    @Override
    public int generateExpiryYear() {
        return LocalDate.now().getYear() + 3;
    }

    @Override
    public int generateExpiryMonth() {
        return LocalDate.now().getMonthValue();
    }

    public static String maskedPan(String pan) {
        final var issuerId = pan.substring(0, 6);
        final var last4 = pan.substring(pan.length() - 4);

        return issuerId + "****" + last4;
    }
}
