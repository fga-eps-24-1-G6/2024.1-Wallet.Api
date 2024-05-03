package com.walletapi.service;

import com.walletapi.dto.StocksDTO;
import com.walletapi.exception.BadRequestNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class StocksService {

    private final RestTemplate restTemplate;
    private final String stocksServiceUrl;

    public StocksService(RestTemplate restTemplate, @Value("${stocks-service.url.base}") String stocksServiceUrl) {
        this.restTemplate = restTemplate;
        this.stocksServiceUrl = stocksServiceUrl;
    }

    public StocksDTO getStocksByTicker(String ticker) {
        ResponseEntity<StocksDTO> response = restTemplate.getForEntity(
                stocksServiceUrl.concat("/api/stocks/stock-summary/" + ticker),
                StocksDTO.class);

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new BadRequestNotFoundException(404, "Could not find stocks with ticker " + ticker);
        }

        return response.getBody();
    }

}
