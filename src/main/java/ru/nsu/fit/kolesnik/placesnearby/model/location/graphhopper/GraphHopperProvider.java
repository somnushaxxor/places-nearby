package ru.nsu.fit.kolesnik.placesnearby.model.location.graphhopper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.nsu.fit.kolesnik.placesnearby.PlacesNearbyApplication;
import ru.nsu.fit.kolesnik.placesnearby.model.location.Location;
import ru.nsu.fit.kolesnik.placesnearby.model.location.LocationsProvider;
import ru.nsu.fit.kolesnik.placesnearby.model.location.graphhopper.payload.GraphHopperResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GraphHopperProvider implements LocationsProvider {

    private static final String API_BASE_URL;
    private static final String API_KEY;

    static {
        Properties properties = new Properties();
        try (InputStream inputStream = PlacesNearbyApplication.class.getResourceAsStream("config.properties")) {
            properties.load(inputStream);
            API_BASE_URL = properties.getProperty("locations.GraphHopper.api.baseUrl");
            API_KEY = properties.getProperty("locations.GraphHopper.api.key");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public GraphHopperProvider(HttpClient httpClient) {
        this.httpClient = httpClient;
        objectMapper = new ObjectMapper();
    }

    @Override
    public void getLocationsByName(String name, Consumer<List<Location>> onSuccess, Consumer<String> onError) {
        String encodedName = getUrlEncodedLocationName(name);
        HttpRequest request = HttpRequest.newBuilder().uri(getUrl(encodedName)).GET().build();
        GraphHopperLocationMapper locationMapper = new GraphHopperLocationMapper();
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> {
                    try {
                        return objectMapper.readValue(json, GraphHopperResponse.class);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                })
                .whenComplete((graphHopperResponse, throwable) -> {
                    if (throwable != null)
                        onError.accept("Failed to process JSON from HTTP request");
                })
                .thenApply(GraphHopperResponse::getHits)
                .thenApply(graphHopperLocations ->
                        graphHopperLocations.stream().map(locationMapper::map).collect(Collectors.toList()))
                .thenAccept(onSuccess);
    }

    private String getUrlEncodedLocationName(String rawName) {
        return rawName.replace(" ", "%20");
    }

    private URI getUrl(String encodedName) {
        try {
            return new URI(String.format("%s/geocode?q=%s&key=%s", API_BASE_URL, encodedName, API_KEY));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
