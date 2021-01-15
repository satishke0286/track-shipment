package com.aftership.tracking.model;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class Checkpoint {
    private Date createdAt;
    private String slug;
    private String checkpointTime;
    private String location;
    private String city;
    private String state;
    private List<String> coordinates;
    private String countryIso3;
    private String countryName;
    private String message;
    private String tag;
    private String subtag;
    private String subtagMessage;
    private String zip;
    private String rawTag;

}
