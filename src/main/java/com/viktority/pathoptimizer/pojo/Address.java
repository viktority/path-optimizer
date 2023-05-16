package com.viktority.pathoptimizer.pojo;


public class Address {
    private String postcode;
    private Integer quality;
    private Integer eastings;
    private Integer northings;
    private String country;
    private String nhsHa;
    private Double longitude;
    private Double latitude;
    private String europeanElectoralRegion;
    private String primaryCareTrust;
    private String region;
    private String lsoa;
    private String msoa;
    private String incode;
    private String outcode;
    private String parliamentaryConstituency;
    private String adminDistrict;
    private String parish;
    private Object adminCounty;
    private String dateOfIntroduction;
    private String adminWard;
    private Object ced;
    private String ccg;
    private String nuts;
    private String pfa;
    private Codes codes;

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Address{" +
                "postcode='" + postcode + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}

