package ru.nsu.fit.kolesnik.placesnearby.model.place.opentripmap.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenTripMapPlaceInfoResponse {

    @JsonProperty("xid")
    private String id;
    private String name;
    @JsonProperty("point")
    private Coordinates coordinates;
    private String kinds;
    @JsonProperty("image")
    private String imageUrl;
    @JsonProperty("wikipedia")
    private String wikipediaUrl;

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    static public class Coordinates {

        @JsonProperty("lat")
        private double latitude;
        @JsonProperty("lon")
        private double longitude;

    }

}
