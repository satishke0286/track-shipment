package com.aftership.tracking.restutil;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.aftership.tracking.exception.ShipmentTrackingException;
import com.aftership.tracking.model.AftershipResponse;
import com.aftership.tracking.model.SingleTracking;
import com.aftership.tracking.model.Tracking;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RestApiClient {

    private static final Logger logger = LoggerFactory.getLogger(RestApiClient.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${aftership-api-key}")
    private String apiKey;

    @Value("${aftership-api-url}")
    private String apiServiceUrl;

    public Tracking makePostRestCall(Object payload) {
        logger.debug("Called aftership post api to create tracking");
        return makeRestCall(apiServiceUrl + "/trackings", HttpMethod.POST, new HttpEntity(payload, getHttpHeaders()));
    }

    public Tracking makeGetRestCall(String trackingNumber, String courierCode) {
        logger.debug("Called aftership get api to create tracking");
        String url = apiServiceUrl + "/trackings/" + courierCode + "/" + trackingNumber;
        return makeRestCall(url, HttpMethod.GET, new HttpEntity(getHttpHeaders()));
    }

    private Tracking makeRestCall(String url, HttpMethod httpMethod, HttpEntity httpEntity) {
        logger.debug("Called aftership api to url {}", url);
        try {
            ResponseEntity<AftershipResponse<SingleTracking>> responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, new ParameterizedTypeReference<AftershipResponse<SingleTracking>>() {
            });
            AftershipResponse<SingleTracking> requestBody = responseEntity.getBody();
            return requestBody.getData().getTracking();
        } catch(HttpClientErrorException exception) {
            try {
                AftershipResponse<SingleTracking> response = new ObjectMapper().readValue(exception.getResponseBodyAsString(), AftershipResponse.class);
                throw new ShipmentTrackingException(response.getMeta().getMessage());
            } catch (IOException ex) {
                throw new ShipmentTrackingException(exception.getResponseBodyAsString());
            }
        } catch (RestClientException restClientException) {
            logger.error("Exception occurred while making REST call for URL: {}", apiServiceUrl);
            throw new ShipmentTrackingException("Exception occurred while making REST call for URL: " + restClientException.getMessage());
        }
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("aftership-api-key", apiKey);
        return headers;
    }

}
