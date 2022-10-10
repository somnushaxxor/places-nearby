package ru.nsu.fit.kolesnik.placesnearby;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.nsu.fit.kolesnik.placesnearby.model.location.Location;
import ru.nsu.fit.kolesnik.placesnearby.model.location.LocationsProvider;
import ru.nsu.fit.kolesnik.placesnearby.model.location.graphhopper.GraphHopperProvider;

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
        LocationsProvider locationsProvider = new GraphHopperProvider(httpClient);
        locationsProvider.getLocationsByName("лицей 64",
                locations -> {
                    for (Location location : locations) {
                        System.out.println(location);
                    }
                },
                System.out::println
        );
    }

    public static void main(String[] args) {
        launch();
    }

}