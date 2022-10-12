package ru.nsu.fit.kolesnik.placesnearby.model.place;

import java.util.List;
import java.util.function.Consumer;

public interface PlacesProvider {

    void getPlacesByCoordinates(double latitude, double longitude, Consumer<List<Place>> onSuccess,
                                Consumer<String> onError);

}
