package com.aftership.tracking.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Tracking {
  private Date createdAt;
  private Date updatedAt;
  private String id;
  private String trackingPostalCode;
  private String trackingShipDate;
  private String trackingAccountNumber;
  private String trackingOriginCountry;
  private String trackingDestinationCountry;
  private String trackingState;
  private String trackingKey;
  private String slug;
  private String trackingNumber;
  private boolean active;
  private List<String> android;
  private Map<String, String> customFields;
  private String customerName;
  private int deliveryTime;
  private String destinationCountryIso3;
  private String courierDestinationCountryIso3;
  private List<String> emails;
  private String expectedDelivery;
  private List<String> ios;
  private String orderId;
  private String orderIdPath;
  private String originCountryIso3;
  private String uniqueToken;
  private int shipmentPackageCount;
  private String shipmentType;
  private int shipmentWeight;
  private String shipmentWeightUnit;
  private Date lastUpdatedAt;
  private Date shipmentPickupDate;
  private Date shipmentDeliveryDate;
  private List<String> subscribedSmses;
  private List<String> subscribedEmails;
  private String signedBy;
  private List<String> smses;
  private String source;
  private String tag;
  private String subtag;
  private String subtagMessage;
  private String title;
  private int trackedCount;
  private Boolean lastMileTrackingSupported;
  private String language;
  private Boolean returnToSender;
  private String orderPromisedDeliveryDate;
  private String deliveryType;
  private String pickupLocation;
  private String pickupNote;
  private String courierTrackingLink;
  private String courierRedirectLink;
  private String firstAttemptedAt;
  private List<Checkpoint> checkpoints;

}
