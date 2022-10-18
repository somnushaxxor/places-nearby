package ru.nsu.fit.kolesnik.placesnearby.model.weather.openweather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.nsu.fit.kolesnik.placesnearby.PlacesNearbyApplication;
import ru.nsu.fit.kolesnik.placesnearby.model.exception.ConfigPropertyNotFoundException;
import ru.nsu.fit.kolesnik.placesnearby.model.weather.Weather;
import ru.nsu.fit.kolesnik.placesnearby.model.weather.WeatherProvider;
import ru.nsu.fit.kolesnik.placesnearby.model.weather.openweather.payload.OpenWeatherResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;
import java.util.function.Consumer;

public class OpenWeatherProvider implements WeatherProvider {

    private static final String API_BASE_URL;
    private static final String API_KEY;

    static {
        Properties properties = new Properties();
        try (InputStream inputStream = PlacesNearbyApplication.class.getResourceAsStream("config.properties")) {
            properties.load(inputStream);
            API_BASE_URL = properties.getProperty("weather.OpenWeather.api.baseUrl");
            if (API_BASE_URL == null) {
                throw new ConfigPropertyNotFoundException("Add OpenWeather API base URL to config file!");
            }
            API_KEY = properties.getProperty("weather.OpenWeather.api.key");
            if (API_KEY == null) {
                throw new ConfigPropertyNotFoundException("Add OpenWeather API key to config file!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OpenWeatherProvider(HttpClient httpClient) {
        this.httpClient = httpClient;
        objectMapper = new ObjectMapper();
    }

    @Override
    public void getWeatherByCoordinates(double latitude, double longitude, Consumer<Weather> onSuccess,
                                        Consumer<String> onError) {
        HttpRequest request = HttpRequest.newBuilder().uri(getUrl(latitude, longitude)).GET().build();
        OpenWeatherMapper weatherMapper = new OpenWeatherMapper();
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> {
                    try {
                        return objectMapper.readValue(json, OpenWeatherResponse.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .whenComplete((openWeatherResponse, throwable) -> {
                    if (throwable != null) {
                        onError.accept("Failed to process JSON from HTTP request");
                    }
                })
                .thenApply(weatherMapper::map)
                .thenAccept(onSuccess);
    }

    private URI getUrl(double latitude, double longitude) {
        try {
            return new URI(String.format("%s/weather?lat=%f&lon=%f&units=metric&appid=%s", API_BASE_URL, latitude,
                    longitude, API_KEY));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
