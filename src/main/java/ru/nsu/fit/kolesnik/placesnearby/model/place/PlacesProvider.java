package ru.nsu.fit.kolesnik.placesnearby.model.place;

import java.util.List;
import java.util.function.Consumer;

public interface PlacesProvider {

    void getPlacesByCoordinatesInRadius(double latitude, double longitude, int radius, Consumer<List<Place>> onSuccess,
                                        Consumer<String> onError);

}
