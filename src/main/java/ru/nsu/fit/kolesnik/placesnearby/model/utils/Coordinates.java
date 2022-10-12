package ru.nsu.fit.kolesnik.placesnearby.model.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Coordinates {

    private final double latitude;
    private final double longitude;

}
