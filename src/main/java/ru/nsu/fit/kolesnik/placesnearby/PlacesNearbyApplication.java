package ru.nsu.fit.kolesnik.placesnearby;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.nsu.fit.kolesnik.placesnearby.model.location.GraphHopperLocationsProvider;
import ru.nsu.fit.kolesnik.placesnearby.model.location.LocationsProvider;

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
        locationsProvider.getLocationsByName("цирк никулина");
    }

    public static void main(String[] args) {
        launch();
    }

}