package com.routes.geolocation.model;

public class UnknownGeoObject extends GeoObject {

    public UnknownGeoObject(String city, String country, Double latitude, Double longitude) {
        super(city, country, latitude, longitude);
    }

    @Override
    public boolean isKnown() {
        return false;
    }
}
