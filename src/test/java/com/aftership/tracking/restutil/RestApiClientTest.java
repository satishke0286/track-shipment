package com.aftership.tracking.restutil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.aftership.tracking.exception.ShipmentTrackingException;
import com.aftership.tracking.model.AftershipResponse;
import com.aftership.tracking.model.NewTrackingRequest;
import com.aftership.tracking.model.SingleTracking;
import com.aftership.tracking.model.Tracking;

@RunWith(MockitoJUnitRunner.class)
public class RestApiClientTest {

    @InjectMocks
    private RestApiClient restApiClient;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(restApiClient, "apiKey", "apiKey_value");
        ReflectionTestUtils.setField(restApiClient, "apiServiceUrl", "apiServiceUrl_value");
    }

    @Test
    public void makePostRestCall_success() {
        ResponseEntity<AftershipResponse<SingleTracking>> myEntity = new ResponseEntity<AftershipResponse<SingleTracking>>(buildSingleTracking(), HttpStatus.ACCEPTED);
        Mockito.when(restTemplate.exchange(
                Mockito.any(String.class),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(ParameterizedTypeReference.class)
                )
        ).thenReturn(myEntity);
        restApiClient.makePostRestCall(new NewTrackingRequest());
        Mockito.verify(restTemplate).exchange(
                Mockito.any(String.class),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(ParameterizedTypeReference.class)
        );
    }

    @Test(expected = ShipmentTrackingException.class)
    public void makePostRestCall_HttpClientErrorException() {
        Mockito.when(restTemplate.exchange(
                Mockito.any(String.class),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(ParameterizedTypeReference.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        restApiClient.makePostRestCall(new NewTrackingRequest());
    }

    @Test(expected = ShipmentTrackingException.class)
    public void makePostRestCall_exception() {
        Mockito.when(restTemplate.exchange(
                Mockito.any(String.class),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(ParameterizedTypeReference.class)
        )).thenThrow(new RestClientException("Error"));
        restApiClient.makePostRestCall(new NewTrackingRequest());
    }

    @Test
    public void makeGetRestCall_success() {
        ResponseEntity<AftershipResponse<SingleTracking>> myEntity = new ResponseEntity<AftershipResponse<SingleTracking>>(buildSingleTracking(), HttpStatus.ACCEPTED);
        Mockito.when(restTemplate.exchange(
                Mockito.any(String.class),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(ParameterizedTypeReference.class)
                )
        ).thenReturn(myEntity);
        restApiClient.makeGetRestCall("99880088008822", "FedEx");
        Mockito.verify(restTemplate).exchange(
                Mockito.any(String.class),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(ParameterizedTypeReference.class)
        );
    }

    @Test(expected = ShipmentTrackingException.class)
    public void makeGetRestCall_exception() {
        Mockito.when(restTemplate.exchange(
                Mockito.any(String.class),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(ParameterizedTypeReference.class))
        ).thenThrow(new RestClientException("Error"));
        restApiClient.makeGetRestCall("99880088008822", "FedEx");
    }

    private AftershipResponse<SingleTracking> buildSingleTracking() {
        SingleTracking singleTracking = new SingleTracking();
        singleTracking.setTracking(new Tracking());
        AftershipResponse<SingleTracking> response = new AftershipResponse<>();
        response.setData(singleTracking);
        return response;
    }

}
