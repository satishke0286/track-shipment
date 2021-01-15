package com.aftership.tracking.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewTracking {
    @JsonProperty("tracking_number")
    private String trackingNumber;
    private String[] slug;
    @JsonProperty("origin_country_iso3")
    private String originCountryIso3;
    @JsonProperty("destination_country_iso3")
    private String destinationCountryIso3;

}
