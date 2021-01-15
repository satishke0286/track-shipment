package com.aftership.tracking.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.aftership.tracking.entity.ShipmentDetails;
import com.aftership.tracking.exception.ShipmentTrackingException;
import com.aftership.tracking.model.ShipmentModel;
import com.aftership.tracking.repository.ShipmentDetailsRepository;
import com.aftership.tracking.restutil.RestApiClient;
import com.aftership.tracking.service.impl.ShipmentServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class ShipmentServiceTest {

    @InjectMocks
    private ShipmentServiceImpl shipmentService;

    @Mock
    private ShipmentDetailsRepository shipmentDetailsRepository;

    @Mock
    private RestApiClient restApiClient;

    @Test(expected = ShipmentTrackingException.class)
    public void create_tracking_null_tracking_number() {
        shipmentService.createTracking(ShipmentModel.builder().build());
    }

    @Test(expected = ShipmentTrackingException.class)
    public void create_tracking_null_origin() {
        shipmentService.createTracking(ShipmentModel.builder().trackingNumber("9374889676091297266845").build());
    }

    @Test(expected = ShipmentTrackingException.class)
    public void create_tracking_null_destination() {
        shipmentService.createTracking(
                ShipmentModel.builder()
                        .trackingNumber("9374889676091297266845")
                        .origin("990 S Hwy 395 S, Hermiston, OR 97838")
                        .build()
        );
    }

    @Test(expected = ShipmentTrackingException.class)
    public void create_tracking_invalid_courier_code() {
        shipmentService.createTracking(
                ShipmentModel.builder()
                        .trackingNumber("9374889676091297266845")
                        .origin("990 S Hwy 395 S, Hermiston, OR 97838")
                        .destination("3505 Factoria Blvd SE, Bellevue, WA 98006")
                        .build()
        );
    }

    @Test(expected = ShipmentTrackingException.class)
    public void create_tracking_shipment_already_exists() {
        when(shipmentDetailsRepository.existsByTrackingNumberAndCourierCode(anyString(), anyString())).thenReturn(true);
        shipmentService.createTracking(
                ShipmentModel.builder()
                        .trackingNumber("9374889676091297266845")
                        .origin("990 S Hwy 395 S, Hermiston, OR 97838")
                        .destination("3505 Factoria Blvd SE, Bellevue, WA 98006")
                        .courierCode("FedEx")
                        .build()
        );
    }

    @Test
    public void create_tracking() {
        when(shipmentDetailsRepository.existsByTrackingNumberAndCourierCode(anyString(), anyString())).thenReturn(false);
        ResponseEntity<Object> myEntity = new ResponseEntity<Object>(HttpStatus.ACCEPTED);
        Mockito.when(restApiClient.makePostRestCall(any(Object.class), any())).thenReturn(myEntity);
        when(shipmentDetailsRepository.save(any(ShipmentDetails.class))).thenReturn(ShipmentDetails.builder().id(UUID.randomUUID().toString()).build());
        ShipmentModel result = shipmentService.createTracking(
                ShipmentModel.builder()
                        .trackingNumber("9374889676091297266845")
                        .origin("990 S Hwy 395 S, Hermiston, OR 97838")
                        .destination("3505 Factoria Blvd SE, Bellevue, WA 98006")
                        .courierCode("FedEx")
                        .build()
        );
        assertNotNull(result.getId());
    }

}
