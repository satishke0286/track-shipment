package com.aftership.tracking.transformation;

import java.util.UUID;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.aftership.tracking.entity.ShipmentDetails;
import com.aftership.tracking.model.ShipmentModel;

@Component
public class ShipmentEntityTransformation implements Function<ShipmentModel, ShipmentDetails> {

    @Override
    public ShipmentDetails apply(ShipmentModel model) {
        ShipmentDetails shipmentDetails = new ShipmentDetails();
        shipmentDetails.setId(UUID.randomUUID().toString());
        shipmentDetails.setCourierCode(model.getCourierCode());
        shipmentDetails.setTrackingNumber(model.getTrackingNumber());
        shipmentDetails.setOrigin(model.getOrigin());
        shipmentDetails.setDestination(model.getDestination());
        return shipmentDetails;
    }

}
