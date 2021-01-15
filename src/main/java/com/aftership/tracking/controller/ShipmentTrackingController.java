package com.aftership.tracking.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aftership.tracking.model.ShipmentModel;
import com.aftership.tracking.service.ShipmentService;

@RestController
@RequestMapping("/tracking")
public class ShipmentTrackingController {

    private static final Logger logger = LoggerFactory.getLogger(ShipmentTrackingController.class);

    @Autowired
    private ShipmentService shipmentService;

    @PostMapping
    public ShipmentModel createTracking(@RequestBody ShipmentModel shipmentModel) {
        logger.debug("ShipmentTrackingController :: called createTracking method for tracking id {} and courier code {} " , shipmentModel.getTrackingNumber(), shipmentModel.getCourierCode());
        return shipmentService.createTracking(shipmentModel);
    }
}
