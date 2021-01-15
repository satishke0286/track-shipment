package com.aftership.tracking.service;

import com.aftership.tracking.model.ShipmentModel;

public interface ShipmentService {
    ShipmentModel createTracking(ShipmentModel shipmentModel);
    ShipmentModel getTracking(String trackingNumber, String courierCode);

}
