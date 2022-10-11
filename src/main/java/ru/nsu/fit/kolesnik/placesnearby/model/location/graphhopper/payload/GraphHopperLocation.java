package ru.nsu.fit.kolesnik.placesnearby.model.location.graphhopper.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GraphHopperLocation {

    private GraphHopperLocationPoint point;
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
    public static class GraphHopperLocationPoint {

        @JsonProperty("lat")
        private double latitude;
        @JsonProperty("lng")
        private double longitude;

    }

}
