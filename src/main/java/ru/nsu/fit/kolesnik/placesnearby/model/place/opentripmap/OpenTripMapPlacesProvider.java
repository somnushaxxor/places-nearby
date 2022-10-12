package ru.nsu.fit.kolesnik.placesnearby.model.place.opentripmap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.nsu.fit.kolesnik.placesnearby.PlacesNearbyApplication;
import ru.nsu.fit.kolesnik.placesnearby.model.place.Place;
import ru.nsu.fit.kolesnik.placesnearby.model.place.PlacesProvider;
import ru.nsu.fit.kolesnik.placesnearby.model.place.opentripmap.payload.OpenTripMapPlaceInfoResponse;
import ru.nsu.fit.kolesnik.placesnearby.model.place.opentripmap.payload.OpenTripMapPlacesResponse;
import ru.nsu.fit.kolesnik.placesnearby.model.place.opentripmap.payload.OpenTripMapPlacesResponseItem;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class OpenTripMapPlacesProvider implements PlacesProvider {

    private static final String API_BASE_URL;
    private static final int RADIUS;
    private static final int LIMIT;
    private static final String API_KEY;

    static {
        Properties properties = new Properties();
        try (InputStream inputStream = PlacesNearbyApplication.class.getResourceAsStream("config.properties")) {
            properties.load(inputStream);
            API_BASE_URL = properties.getProperty("places.OpenTripMap.api.baseUrl");
            RADIUS = Integer.parseInt(properties.getProperty("places.OpenTripMap.api.radius"));
            LIMIT = Integer.parseInt(properties.getProperty("places.OpenTripMap.api.limit"));
            API_KEY = properties.getProperty("places.OpenTripMap.api.key");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OpenTripMapPlacesProvider(HttpClient httpClient) {
        this.httpClient = httpClient;
        objectMapper = new ObjectMapper();
    }

    @Override
    public void getPlacesByCoordinates(double latitude, double longitude, Consumer<List<Place>> onSuccess,
                                       Consumer<String> onError) {
        HttpRequest placesRequest = HttpRequest.newBuilder().uri(getPlacesByCoordinatesUrl(latitude, longitude)).GET()
                .build();
        httpClient.sendAsync(placesRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> {
                    try {
                        return objectMapper.readValue(json, OpenTripMapPlacesResponse.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .whenComplete((openTripMapPlacesResponse, throwable) -> {
                    if (throwable != null) {
                        onError.accept("Failed to process JSON from HTTP request");
                    }
                })
                .thenAccept(openTripMapPlacesResponse ->
                        getPlacesInfoByPlaces(openTripMapPlacesResponse, onSuccess, onError)
                );
    }

    private void getPlacesInfoByPlaces(OpenTripMapPlacesResponse places, Consumer<List<Place>> onSuccess,
                                       Consumer<String> onError) {
        OpenTripMapPlaceMapper placeMapper = new OpenTripMapPlaceMapper();
        List<CompletableFuture<OpenTripMapPlaceInfoResponse>> placeInfoCompletableFutures = new ArrayList<>();
        for (OpenTripMapPlacesResponseItem place : places) {
            HttpRequest placesInfoRequest = HttpRequest.newBuilder().uri(getPlaceInfoByPlaceIdUrl(place.getId())).GET()
                    .build();
            var placeInfoCompletableFuture = httpClient.sendAsync(placesInfoRequest, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(json -> {
                        try {
                            return objectMapper.readValue(json, OpenTripMapPlaceInfoResponse.class);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .whenComplete((openTripMapPlaceInfoResponse, throwable) -> {
                        if (throwable != null) {
                            onError.accept("Failed to process JSON from HTTP request");
                        }
                    });
            placeInfoCompletableFutures.add(placeInfoCompletableFuture);
        }
        CompletableFuture.allOf(placeInfoCompletableFutures.toArray(new CompletableFuture[0]))
                .thenApply(v -> placeInfoCompletableFutures.stream()
                        .map(CompletableFuture::join)
                        .filter(openTripMapPlaceInfoResponse -> openTripMapPlaceInfoResponse.getId() != null)
                        .map(placeMapper::map)
                        .collect(Collectors.toList()))
                .thenAccept(onSuccess);
    }

    private URI getPlacesByCoordinatesUrl(double latitude, double longitude) {
        try {
            return new URI(String.format(Locale.US, "%s/radius?radius=%d&lon=%f&lat=%f&format=json&limit=%d&apikey=%s",
                    API_BASE_URL, RADIUS, longitude, latitude, LIMIT, API_KEY));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private URI getPlaceInfoByPlaceIdUrl(String placeId) {
        try {
            return new URI(String.format("%s/xid/%s?apikey=%s", API_BASE_URL, placeId, API_KEY));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
