package ru.nsu.fit.kolesnik.placesnearby;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.nsu.fit.kolesnik.placesnearby.model.location.LocationsProvider;
import ru.nsu.fit.kolesnik.placesnearby.model.location.graphhopper.GraphHopperLocationsProvider;
import ru.nsu.fit.kolesnik.placesnearby.model.place.Place;
import ru.nsu.fit.kolesnik.placesnearby.model.place.PlacesProvider;
import ru.nsu.fit.kolesnik.placesnearby.model.place.opentripmap.OpenTripMapPlacesProvider;
import ru.nsu.fit.kolesnik.placesnearby.model.weather.WeatherProvider;
import ru.nsu.fit.kolesnik.placesnearby.model.weather.openweather.OpenWeatherProvider;

import java.io.IOException;
import java.net.http.HttpClient;

public class PlacesNearbyApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(PlacesNearbyApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stage.setTitle("Hello!");
//        stage.setScene(scene);
        stage.show();

        HttpClient httpClient = HttpClient.newHttpClient();
        LocationsProvider locationsProvider = new GraphHopperLocationsProvider(httpClient);
        locationsProvider.getLocationsByName("лицей 64",
                locations -> {
                    WeatherProvider weatherProvider = new OpenWeatherProvider(httpClient);
                    weatherProvider.getWeatherByCoordinates(locations.get(3).getCoordinates().getLatitude(),
                            locations.get(3).getCoordinates().getLongitude(),
                            System.out::println, System.out::println);
                    PlacesProvider placesProvider = new OpenTripMapPlacesProvider(httpClient);
                    placesProvider.getPlacesByCoordinates(locations.get(3).getCoordinates().getLatitude(),
                            locations.get(3).getCoordinates().getLongitude(),
                            places -> {
                                for (Place place : places) {
                                    System.out.println(place);
                                }
                            }, System.out::println);
                },
                System.out::println
        );
    }

    public static void main(String[] args) {
        launch();
    }

}