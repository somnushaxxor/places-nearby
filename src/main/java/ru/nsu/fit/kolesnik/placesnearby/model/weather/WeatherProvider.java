package ru.nsu.fit.kolesnik.placesnearby.model.weather;

import java.util.function.Consumer;

public interface WeatherProvider {

    void getWeatherByCoordinates(double latitude, double longitude, Consumer<Weather> onSuccess,
                                 Consumer<String> onError);

}
