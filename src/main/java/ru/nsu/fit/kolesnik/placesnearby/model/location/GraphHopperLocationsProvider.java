package ru.nsu.fit.kolesnik.placesnearby.model.location;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.nsu.fit.kolesnik.placesnearby.PlacesNearbyApplication;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class GraphHopperLocationsProvider implements LocationsProvider {

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

    public GraphHopperLocationsProvider(HttpClient httpClient) {
        this.httpClient = httpClient;
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public void getLocationsByName(String name) {
        String encodedName = getUrlEncodedLocationName(name);
        URI requestURI;
        try {
            requestURI = new URI(String.format("%s/geocode?q=%s&key=%s", API_BASE_URL, encodedName, API_KEY));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        HttpRequest request = HttpRequest.newBuilder().uri(requestURI).GET().build();
        try {
            String responseJson = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get();
            System.out.println(responseJson);
            String locationsArrayJson = objectMapper.readTree(responseJson).get("hits").toString();
            List<GeocodingLocation> locationList = objectMapper.readValue(locationsArrayJson, new TypeReference<>() {
            });
            for (GeocodingLocation location : locationList) {
                System.out.println(location);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private String getUrlEncodedLocationName(String rawName) {
        return rawName.replace(" ", "%20");
    }

}
