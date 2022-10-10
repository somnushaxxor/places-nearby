package ru.nsu.fit.kolesnik.placesnearby.model.location;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Location {

    private final String name;
    private final double latitude;
    private final double longitude;
    private final String country;
    private final String state;
    private final String city;
    private final String street;
    private final String houseNumber;
    private final String postcode;


    public Location(String name, double latitude, double longitude, String country, String state, String city,
                    String street, String houseNumber, String postcode) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.state = state;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.postcode = postcode;
    }

}
