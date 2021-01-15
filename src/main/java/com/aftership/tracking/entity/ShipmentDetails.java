package com.aftership.tracking.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shipment_details")
@Builder
public class ShipmentDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "courier_code")
    private String courierCode;

    @Column(name = "origin")
    private String origin;

    @Column(name = "destination")
    private String destination;

}
