package ru.nsu.fit.kolesnik.placesnearby.model.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.nsu.fit.kolesnik.placesnearby.model.utils.Coordinates;

import java.util.Locale;

@Getter
@AllArgsConstructor
public class Location {

    private final String name;
    private final Coordinates coordinates;
    private final String country;
    private final String state;
    private final String city;
    private final String street;
    private final String houseNumber;
    private final String postcode;

    @Override
    public String toString() {
        return String.format(Locale.US, "%s\n%.3f %.3f", name, coordinates.getLatitude(),
                coordinates.getLongitude()) +
                "\nCountry: " + country +
                "\nState: " + state +
                "\nCity: " + city +
                "\nStreet: " + street +
                "\nHouse number: " + houseNumber +
                "\nPostcode: " + postcode;
    }

}
