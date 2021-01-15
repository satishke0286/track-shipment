package com.aftership.tracking.restutil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.aftership.tracking.exception.ShipmentTrackingException;
import com.aftership.tracking.model.AftershipResponse;
import com.aftership.tracking.model.NewTrackingRequest;

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
        ResponseEntity<Void> myEntity = new ResponseEntity<Void>(HttpStatus.ACCEPTED);
        Mockito.when(restTemplate.exchange(
                Matchers.anyString(),
                Matchers.any(HttpMethod.class),
                Matchers.any(),
                Matchers.<Class<Void>>any())
        ).thenReturn(myEntity);
        restApiClient.makePostRestCall(new NewTrackingRequest(), AftershipResponse.class);
        Mockito.verify(restTemplate).exchange( Matchers.anyString(),
                Matchers.any(HttpMethod.class),
                Matchers.any(),
                Matchers.<Class<Void>>any());
    }

    @Test(expected = ShipmentTrackingException.class)
    public void makePostRestCall_exception() {
        Mockito.when(restTemplate.exchange(
                Matchers.anyString(),
                Matchers.any(HttpMethod.class),
                Matchers.any(),
                Matchers.<Class<Void>>any())
        ).thenThrow(new RestClientException("Error"));
        restApiClient.makePostRestCall(new NewTrackingRequest(), AftershipResponse.class);
    }

}
