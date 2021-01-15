package com.aftership.tracking.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.aftership.tracking.entity.ShipmentDetails;
import com.aftership.tracking.exception.ShipmentTrackingException;
import com.aftership.tracking.model.Checkpoint;
import com.aftership.tracking.model.NewTracking;
import com.aftership.tracking.model.ShipmentModel;
import com.aftership.tracking.model.Tracking;
import com.aftership.tracking.repository.ShipmentDetailsRepository;
import com.aftership.tracking.restutil.RestApiClient;
import com.aftership.tracking.service.impl.ShipmentServiceImpl;
import com.aftership.tracking.transformation.ShipmentEntityTransformation;
import com.aftership.tracking.transformation.ShipmentModelTransformation;
import com.aftership.tracking.transformation.TrackingTransformation;

@RunWith(MockitoJUnitRunner.class)
public class ShipmentServiceTest {

    @InjectMocks
    private ShipmentServiceImpl shipmentService;

    @Mock
    private ShipmentDetailsRepository shipmentDetailsRepository;

    @Mock
    private RestApiClient restApiClient;

    @Mock
    private TrackingTransformation trackingTransformation;

    @Mock
    private ShipmentEntityTransformation shipmentEntityTransformation;

    @Mock
    private ShipmentModelTransformation shipmentModelTransformation;


    @Test(expected = ShipmentTrackingException.class)
    public void create_tracking_null_tracking_number() {
        shipmentService.createTracking(ShipmentModel.builder().build());
    }

    @Test(expected = ShipmentTrackingException.class)
    public void create_tracking_invalid_courier_code() {
        shipmentService.createTracking(ShipmentModel.builder().trackingNumber("9374889676091297266845").build());
    }

    @Test(expected = ShipmentTrackingException.class)
    public void create_tracking_origin_Null() {
        shipmentService.createTracking(
                ShipmentModel.builder()
                        .courierCode("FedEx")
                        .trackingNumber("9374889676091297266845")
                        .destination("990 S Hwy 395 S, Hermiston, OR 97838")
                        .build()
        );
    }

    @Test(expected = ShipmentTrackingException.class)
    public void create_tracking_destination_Null() {
        shipmentService.createTracking(
                ShipmentModel.builder()
                        .trackingNumber("9374889676091297266845")
                        .courierCode("FedEx")
                        .origin("990 S Hwy 395 S, Hermiston, OR 97838")
                        .build()
        );
    }

    @Test(expected = ShipmentTrackingException.class)
    public void create_tracking_shipment_already_exists() {
        when(shipmentDetailsRepository.existsByTrackingNumberAndCourierCode(anyString(), anyString())).thenReturn(true);
        shipmentService.createTracking(buildShipmentModel());
    }

    @Test
    public void create_tracking_success() {
        when(shipmentDetailsRepository.existsByTrackingNumberAndCourierCode(anyString(), anyString())).thenReturn(false);
        when(trackingTransformation.apply(any(ShipmentModel.class))).thenReturn(new NewTracking());
        ShipmentModel shipmentModel = buildShipmentModel();
        shipmentModel.setId(UUID.randomUUID().toString());
        when(shipmentModelTransformation.apply(any(ShipmentDetails.class))).thenReturn(shipmentModel);
        Mockito.when(restApiClient.makePostRestCall(any(Object.class))).thenReturn(new Tracking());
        when(shipmentDetailsRepository.save(any(ShipmentDetails.class))).thenReturn(ShipmentDetails.builder().id(UUID.randomUUID().toString()).build());
        ShipmentModel result = shipmentService.createTracking(buildShipmentModel());
        assertNotNull(result.getId());
    }

    @Test(expected = ShipmentTrackingException.class)
    public void getTracking_trackingNumber_Null() {
        shipmentService.getTracking(null, "FedEx");
    }

    @Test(expected = ShipmentTrackingException.class)
    public void getTracking_Courier_Code_Null() {
        shipmentService.getTracking("9374889676091297266845", "");
    }

    @Test(expected = ShipmentTrackingException.class)
    public void getTracking_Courier_Code_Not_Found() {
        when(shipmentDetailsRepository.findByTrackingNumberAndCourierCode(anyString(), anyString())).thenReturn(null);
        shipmentService.getTracking("9374889676091297266845", "FedEx");
    }

    @Test
    public void getTracking_Courier_Code_Success() {
        when(shipmentDetailsRepository.findByTrackingNumberAndCourierCode(anyString(), anyString())).thenReturn(buildShipmentDetails());
        when(restApiClient.makeGetRestCall(anyString(), anyString())).thenReturn(buildTracking());
        when(shipmentModelTransformation.apply(any())).thenReturn(buildShipmentModel());
        ShipmentModel response = shipmentService.getTracking("9374889676091297266845", "FedEx");
        assertEquals("In Transit", response.getCurrentStatus());
    }

    @Test
    public void getTracking_Courier_Code_Checkpoints_Success() {
        when(shipmentDetailsRepository.findByTrackingNumberAndCourierCode(anyString(), anyString())).thenReturn(buildShipmentDetails());
        when(restApiClient.makeGetRestCall(anyString(), anyString())).thenReturn(buildTrackingWithCheckPoints());
        when(shipmentModelTransformation.apply(any())).thenReturn(buildShipmentModel());
        ShipmentModel response = shipmentService.getTracking("9374889676091297266845", "FedEx");
        assertEquals("Delivered", response.getCurrentStatus());
    }

    private ShipmentModel buildShipmentModel() {
        return ShipmentModel.builder()
                .trackingNumber("9374889676091297266845")
                .origin("990 S Hwy 395 S, Hermiston, OR 97838")
                .destination("3505 Factoria Blvd SE, Bellevue, WA 98006")
                .courierCode("FedEx")
                .build();
    }

    private Tracking buildTracking() {
        Tracking tracking = new Tracking();
        tracking.setTag("In Transit");
        return tracking;
    }

    private Tracking buildTrackingWithCheckPoints() {
        Tracking tracking = new Tracking();
        tracking.setTag("Delivered");
        Checkpoint origin = new Checkpoint();
        origin.setLocation("Hermiston, OR 97838");
        Checkpoint destination = new Checkpoint();
        destination.setLocation("Bellevue, WA 98006");
        tracking.setCheckpoints(Arrays.asList(origin, destination));
        return tracking;
    }

    private ShipmentDetails buildShipmentDetails() {
        return ShipmentDetails.builder()
                .id(UUID.randomUUID().toString())
                .trackingNumber("9374889676091297266845")
                .origin("990 S Hwy 395 S, Hermiston, OR 97838")
                .destination("3505 Factoria Blvd SE, Bellevue, WA 98006")
                .courierCode("FedEx")
                .build();
    }

}
