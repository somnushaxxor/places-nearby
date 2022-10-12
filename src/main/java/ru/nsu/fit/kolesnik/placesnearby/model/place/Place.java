package ru.nsu.fit.kolesnik.placesnearby.model.place;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import ru.nsu.fit.kolesnik.placesnearby.model.utils.Coordinates;

@Getter
@ToString
@AllArgsConstructor
public class Place {

    private final String name;
    private final Coordinates coordinates;
    private final String kinds;
    private final String imageUrl;
    private final String wikipediaUrl;

}
