package ru.nsu.fit.kolesnik.placesnearby.model.place;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.nsu.fit.kolesnik.placesnearby.model.utils.Coordinates;

import java.util.Locale;

@Getter
@AllArgsConstructor
public class Place {

    private final String name;
    private final Coordinates coordinates;
    private final String kinds;
    private final String imageUrl;
    private final String wikipediaUrl;

    @Override
    public String toString() {
        return String.format(Locale.US, "%s\n%.3f %.3f\n%s", name, coordinates.getLatitude(),
                coordinates.getLongitude(), kinds);
    }

}
