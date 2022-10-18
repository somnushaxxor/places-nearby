package ru.nsu.fit.kolesnik.placesnearby.model.place.opentripmap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.nsu.fit.kolesnik.placesnearby.PlacesNearbyApplication;
import ru.nsu.fit.kolesnik.placesnearby.model.exception.ConfigPropertyNotFoundException;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class OpenTripMapPlacesProvider implements PlacesProvider {

    private static final String API_BASE_URL;
    private static final int LIMIT;
    private static final String API_KEY;

    static {
        Properties properties = new Properties();
        try (InputStream inputStream = PlacesNearbyApplication.class.getResourceAsStream("config.properties")) {
            properties.load(inputStream);
            API_BASE_URL = properties.getProperty("places.OpenTripMap.api.baseUrl");
            if (API_BASE_URL == null) {
                throw new ConfigPropertyNotFoundException("Add OpenTripMap API base URL to config file!");
            }
            String limitString = properties.getProperty("places.OpenTripMap.api.limit");
            if (limitString == null) {
                throw new ConfigPropertyNotFoundException("Add OpenTripMap API limit to config file!");
            }
            LIMIT = Integer.parseInt(limitString);
            API_KEY = properties.getProperty("places.OpenTripMap.api.key");
            if (API_KEY == null) {
                throw new ConfigPropertyNotFoundException("Add OpenTripMap API key to config file!");
            }
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
    public void getPlacesByCoordinatesInRadius(double latitude, double longitude, int radius,
                                               Consumer<List<Place>> onSuccess, Consumer<String> onError) {
        HttpRequest placesRequest = HttpRequest.newBuilder()
                .uri(getPlacesByCoordinatesInRadiusUrl(latitude, longitude, radius)).GET().build();
        httpClient.sendAsync(placesRequest, HttpResponse.BodyHandlers.ofString())
                .thenAccept(httpResponse -> {
                    if (httpResponse.statusCode() == 429) {
                        onError.accept("Too many requests! Try again!");
                    } else {
                        parsePlacesJson(httpResponse.body(), onSuccess, onError);
                    }
                });
    }

    private void parsePlacesJson(String json, Consumer<List<Place>> onSuccess, Consumer<String> onError) {
        OpenTripMapPlacesResponse openTripMapPlacesResponse;
        try {
            openTripMapPlacesResponse = objectMapper.readValue(json, OpenTripMapPlacesResponse.class);
        } catch (JsonProcessingException e) {
            onError.accept("Failed to process JSON from HTTP request");
            throw new RuntimeException(e);
        }
        getPlacesInfoByPlaces(openTripMapPlacesResponse, onSuccess, onError);
    }

    private void getPlacesInfoByPlaces(OpenTripMapPlacesResponse places, Consumer<List<Place>> onSuccess,
                                       Consumer<String> onError) {
        OpenTripMapPlaceMapper placeMapper = new OpenTripMapPlaceMapper();
        List<CompletableFuture<Void>> placeInfoCompletableFutures = new ArrayList<>();
        List<OpenTripMapPlaceInfoResponse> placeInfoList = new ArrayList<>();
        Lock placeInfoListLock = new ReentrantLock();
        for (OpenTripMapPlacesResponseItem place : places) {
            HttpRequest placesInfoRequest = HttpRequest.newBuilder().uri(getPlaceInfoByPlaceIdUrl(place.getId())).GET()
                    .build();
            var placeInfoCompletableFuture = httpClient.sendAsync(placesInfoRequest, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(httpResponse -> {
                        if (httpResponse.statusCode() == 429) {
                            onError.accept("Too many requests! Try again!");
                        } else {
                            parsePlaceInfoJson(httpResponse.body(), placeInfoList, placeInfoListLock, onError);
                        }
                    });
            placeInfoCompletableFutures.add(placeInfoCompletableFuture);
        }
        CompletableFuture.allOf(placeInfoCompletableFutures.toArray(new CompletableFuture[0]))
                .thenApply(v -> placeInfoList.stream()
                        .map(placeMapper::map)
                        .collect(Collectors.toList()))
                .thenAccept(onSuccess);
    }

    private void parsePlaceInfoJson(String json, List<OpenTripMapPlaceInfoResponse> placeInfoList,
                                    Lock placeInfoListLock, Consumer<String> onError) {
        try {
            placeInfoListLock.lock();
            placeInfoList.add(objectMapper.readValue(json, OpenTripMapPlaceInfoResponse.class));
            placeInfoListLock.unlock();
        } catch (JsonProcessingException e) {
            onError.accept("Failed to process JSON from HTTP request");
            throw new RuntimeException(e);
        }
    }

    private URI getPlacesByCoordinatesInRadiusUrl(double latitude, double longitude, int radius) {
        try {
            return new URI(String.format(Locale.US,
                    "%s/radius?radius=%d&lon=%f&lat=%f&format=json&limit=%d&apikey=%s",
                    API_BASE_URL, radius, longitude, latitude, LIMIT, API_KEY));
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
