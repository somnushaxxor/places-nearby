package ru.nsu.fit.kolesnik.placesnearby.model.weather;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Weather {

    private final String name;
    private final String iconUrl;
    private final double temperature;
    private final int humidity;
    private final double windSpeed;

}
