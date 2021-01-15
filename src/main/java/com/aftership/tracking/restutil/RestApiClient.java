package com.aftership.tracking.restutil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.aftership.tracking.exception.ShipmentTrackingException;

@Component
public class RestApiClient {

    private static final Logger logger = LoggerFactory.getLogger(RestApiClient.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${aftership-api-key}")
    private String apiKey;

    @Value("${aftership-api-url}")
    private String apiServiceUrl;

    public void makePostRestCall(Object payload) {
        logger.debug("Calling aftership post api to create tracking");
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("aftership-api-key", apiKey);
            HttpEntity httpEntity = new HttpEntity(payload, headers);
            restTemplate.exchange(apiServiceUrl+"/trackings", HttpMethod.POST, httpEntity, Void.class);
        } catch (RestClientException restClientException) {
            logger.error("Exception occurred while making REST call for URL: {}", apiServiceUrl);
            throw new ShipmentTrackingException("Exception occurred while making REST call for URL: " + restClientException.getMessage());
        }
    }

}
