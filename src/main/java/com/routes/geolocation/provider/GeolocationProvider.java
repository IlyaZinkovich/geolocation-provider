package com.routes.geolocation.provider;

import com.google.maps.GeoApiContext;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.routes.geolocation.model.GeoObject;
import com.routes.geolocation.model.UnknownGeoObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.maps.GeocodingApi.geocode;
import static com.google.maps.model.AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1;
import static com.google.maps.model.AddressComponentType.COUNTRY;
import static com.google.maps.model.AddressComponentType.LOCALITY;

public class GeolocationProvider {

    private GeoApiContext context;

    private Map<String, GeoObject> geoObjectCache = new ConcurrentHashMap<>();

    public GeolocationProvider(String googleApiKey) {
        this.context = new GeoApiContext().setApiKey(googleApiKey);;
    }

    public GeoObject findGeoObject(String name) {
        GeocodingResult[] geocodingResults = getLocation(name, context);
        if (geocodingResults.length == 0)
            return new UnknownGeoObject(name, name, null, null);
        GeocodingResult geocodingResult = geocodingResults[0];
        GeoObject knownGeoObject = getKnownGeoObject(geocodingResult);
        geoObjectCache.putIfAbsent(name, knownGeoObject);
        return knownGeoObject;
    }

    private GeoObject getKnownGeoObject(GeocodingResult geocodingResult) {
        String city = null;
        String country = null;
        String administrativeAreaLevel1 = null;
        for (AddressComponent addressComponent : geocodingResult.addressComponents) {
            AddressComponentType type = addressComponent.types[0];
            if (LOCALITY.equals(type))
                city = addressComponent.longName;
            else if (COUNTRY.equals(type))
                country = addressComponent.longName;
            else if(ADMINISTRATIVE_AREA_LEVEL_1.equals(type))
                administrativeAreaLevel1 = addressComponent.longName;
            if (city != null && country != null) break;
        }
        if (city == null) city = administrativeAreaLevel1;
        return new GeoObject(city, country,
                geocodingResult.geometry.location.lat, geocodingResult.geometry.location.lng);
    }

    private GeocodingResult[] getLocation(String address, GeoApiContext context) {
        try {
            return geocode(context, address).await();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
