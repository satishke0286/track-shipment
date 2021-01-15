package com.aftership.tracking.repository;

import org.springframework.data.repository.CrudRepository;

import com.aftership.tracking.entity.ShipmentDetails;

public interface ShipmentDetailsRepository extends CrudRepository<ShipmentDetails, String> {

    Boolean existsByTrackingNumberAndCourierCode(String trackingNumber, String courierCode);
    ShipmentDetails findByTrackingNumberAndCourierCode(String trackingNumber, String courierCode);

}
