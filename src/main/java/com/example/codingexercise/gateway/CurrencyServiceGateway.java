package com.example.codingexercise.gateway;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class CurrencyServiceGateway {

    private final RestTemplate restTemplate;

    public CurrencyServiceGateway(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public double getExchangeRate(String from, String to) {
        if (from.equalsIgnoreCase(to)) {
            return 1.0;
        }
        String url = String.format("https://api.frankfurter.app/latest?from=%s&to=%s", from, to);
        try {
            ExchangeRateResponse response = restTemplate.getForObject(url, ExchangeRateResponse.class);
            if (response != null && response.rates() != null) {
                return response.rates().get(to.toUpperCase());
            }
        } catch (Exception e) {
            // Fallback or rethrow. For now, returning 1.0 or throwing exception could be options.
            // Given requirements, maybe we should log and throw.
            throw new RuntimeException("Failed to get exchange rate", e);
        }
        throw new RuntimeException("Failed to get exchange rate");
    }

    record ExchangeRateResponse(Map<String, Double> rates) {}
}
