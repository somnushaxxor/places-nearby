package ru.nsu.fit.kolesnik.placesnearby.model.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public final class GeocodingLocation {

    private GeocodingPoint point;
    private String name;
    private String country;
    private String state;
    private String city;
    private String street;
    @JsonProperty("housenumber")
    private String houseNumber;
    private String postcode;

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    private class GeocodingPoint {

        private double lat;
        private double lng;

    }

}
