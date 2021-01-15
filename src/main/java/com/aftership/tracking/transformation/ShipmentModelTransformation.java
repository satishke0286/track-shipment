package com.aftership.tracking.transformation;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.aftership.tracking.entity.ShipmentDetails;
import com.aftership.tracking.model.ShipmentModel;

@Component
public class ShipmentModelTransformation implements Function<ShipmentDetails, ShipmentModel> {

    @Override
    public ShipmentModel apply(ShipmentDetails entity) {
        ShipmentModel model = new ShipmentModel();
        model.setId(entity.getId());
        model.setCourierCode(entity.getCourierCode());
        model.setTrackingNumber(entity.getTrackingNumber());
        model.setOrigin(entity.getOrigin());
        model.setDestination(entity.getDestination());
        return model;
    }

}
