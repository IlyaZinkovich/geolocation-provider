package com.routes.geolocation.model;

public class GeoObject {

    private String city;
    private String country;
    private Double latitude;
    private Double longitude;

    public GeoObject(String city, String country, Double latitude, Double longitude) {
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public boolean isKnown() {
        return true;
    }
}
