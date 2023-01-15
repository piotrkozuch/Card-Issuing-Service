package io.github.piotrkozuch.issuing.config;

import io.github.piotrkozuch.issuing.card.repository.CardJpaRepository;
import io.github.piotrkozuch.issuing.card.repository.CardRepository;
import io.github.piotrkozuch.issuing.card.repository.CardSensitiveDetailsJpaRepository;
import io.github.piotrkozuch.issuing.cardholder.repository.CachingCardholderRepository;
import io.github.piotrkozuch.issuing.cardholder.repository.CardholderCache;
import io.github.piotrkozuch.issuing.cardholder.repository.CardholderJpaRepository;
import io.github.piotrkozuch.issuing.cardholder.repository.CardholderRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public CardholderRepository cardholderRepository(CardholderJpaRepository cardholderJpaRepository,
                                                     CardholderCache cardholderCache) {
        return new CachingCardholderRepository(cardholderJpaRepository, cardholderCache);
    }

    @Bean
    public CardholderCache cardholderCache(CardholderJpaRepository cardholderJpaRepository) {
        return new CardholderCache(cardholderJpaRepository);
    }

    @Bean
    public CardRepository cardRepository(CardJpaRepository cardJpaRepository,
                                         CardSensitiveDetailsJpaRepository cardSensitiveDetailsJpaRepository) {
        return new CardRepository(cardJpaRepository, cardSensitiveDetailsJpaRepository);
    }
}
