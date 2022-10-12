package ru.nsu.fit.kolesnik.placesnearby.model.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import ru.nsu.fit.kolesnik.placesnearby.model.utils.Coordinates;

@Getter
@ToString
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

}
