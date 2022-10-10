package ru.nsu.fit.kolesnik.placesnearby.model.location;

import java.util.List;
import java.util.function.Consumer;

public interface LocationsProvider {

    void getLocationsByName(String name, Consumer<List<Location>> onSuccess, Consumer<String> onError);

}
