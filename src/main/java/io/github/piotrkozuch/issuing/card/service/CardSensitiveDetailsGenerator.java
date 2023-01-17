package io.github.piotrkozuch.issuing.card.service;

import io.github.piotrkozuch.issuing.card.action.exception.CardBrandNotSupportedException;
import io.github.piotrkozuch.issuing.card.repository.CardRepository;
import io.github.piotrkozuch.issuing.common.types.CardBrand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;

import static io.github.piotrkozuch.issuing.card.service.LuhnCheck.calculateControlDigit;
import static io.github.piotrkozuch.issuing.card.service.LuhnCheck.isValidCardNumber;
import static io.github.piotrkozuch.issuing.common.types.CardBrand.MAESTRO;
import static io.github.piotrkozuch.issuing.common.types.CardBrand.MASTERCARD;
import static io.github.piotrkozuch.issuing.common.types.CardBrand.VISA;
import static io.github.piotrkozuch.issuing.utils.Checks.checkRequired;
import static java.lang.String.format;

@Service
public class CardSensitiveDetailsGenerator implements CardSensitiveDetailsService {

    private final CardRepository cardRepository;

    @Autowired
    public CardSensitiveDetailsGenerator(CardRepository cardRepository) {
        this.cardRepository = checkRequired("cardRepository", cardRepository);
    }

    @Override
    public String generateCvv() {
        final var random = new Random();
        return format("%03d", random.nextInt(1000));
    }

    @Override
    public String generatePan(CardBrand cardBrand) {
        var pan = generateNewPan(cardBrand);

        while (!isValidCardNumber(pan) || cardRepository.findByPan(pan).isPresent()) {
            pan = generateNewPan(cardBrand);
        }

        return pan;
    }

    private String generateNewPan(CardBrand cardBrand) {
        if (cardBrand == VISA) {
            return createPan("4", 14);
        }
        if (cardBrand == MASTERCARD) {
            return createPan("51", 13);
        }

        if (cardBrand == MAESTRO) {
            return createPan("55", 13);
        }

        throw new CardBrandNotSupportedException(cardBrand);
    }

    private String createPan(String networkCode, int size) {
        var num = networkCode + genRandomPayload(size);
        return num + calculateControlDigit(num);
    }

    private String genRandomPayload(int size) {
        final var random = new Random();
        final var builder = new StringBuilder();

        for (var i = 0; i < size; i++) {
            var nextNumber = random.nextInt(0, 9);
            builder.append(nextNumber);
        }

        return builder.toString();
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
