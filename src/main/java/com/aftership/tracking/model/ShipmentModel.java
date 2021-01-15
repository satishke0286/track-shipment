package com.aftership.tracking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipmentModel {
    private String id;
    private String origin;
    private String destination;
    private String currentStatus;
    private String trackingNumber;
    private String courierCode;
}
