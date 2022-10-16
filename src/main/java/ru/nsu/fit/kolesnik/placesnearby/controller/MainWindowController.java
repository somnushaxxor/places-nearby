package ru.nsu.fit.kolesnik.placesnearby.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ru.nsu.fit.kolesnik.placesnearby.model.location.Location;
import ru.nsu.fit.kolesnik.placesnearby.model.location.LocationsProvider;
import ru.nsu.fit.kolesnik.placesnearby.model.location.graphhopper.GraphHopperLocationsProvider;
import ru.nsu.fit.kolesnik.placesnearby.model.place.Place;
import ru.nsu.fit.kolesnik.placesnearby.model.place.PlacesProvider;
import ru.nsu.fit.kolesnik.placesnearby.model.place.opentripmap.OpenTripMapPlacesProvider;
import ru.nsu.fit.kolesnik.placesnearby.model.utils.Coordinates;
import ru.nsu.fit.kolesnik.placesnearby.model.weather.WeatherProvider;
import ru.nsu.fit.kolesnik.placesnearby.model.weather.openweather.OpenWeatherProvider;

import java.net.URL;
import java.net.http.HttpClient;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    @FXML
    private TextField searchTextField;
    @FXML
    private Slider radiusSlider;
    @FXML
    private Label radiusLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private ListView<Location> locationsListView;
    @FXML
    private ImageView weatherImageView;
    @FXML
    private Label weatherNameLabel;
    @FXML
    private Label weatherTemperatureLabel;
    @FXML
    private Label weatherHumidityLabel;
    @FXML
    private Label weatherWindSpeedLabel;
    @FXML
    private ListView<Place> placesListView;
    @FXML
    private TextArea placeAdditionalInfoTextArea;

    private LocationsProvider locationsProvider;
    private WeatherProvider weatherProvider;
    private PlacesProvider placesProvider;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        HttpClient httpClient = HttpClient.newHttpClient();
        locationsProvider = new GraphHopperLocationsProvider(httpClient);
        weatherProvider = new OpenWeatherProvider(httpClient);
        placesProvider = new OpenTripMapPlacesProvider(httpClient);
        radiusSlider.valueProperty().addListener((obs, oldval, newVal) ->
                radiusLabel.setText(String.valueOf(Math.round(newVal.doubleValue()))));
    }

    @FXML
    public void onEnteredLocationName() {
        if (!searchTextField.getText().isBlank()) {
            errorLabel.setText("");
            locationsListView.getItems().clear();
            placesListView.getItems().clear();
            locationsProvider.getLocationsByName(searchTextField.getText(),
                    locations ->
                            Platform.runLater(() -> locations.forEach(location ->
                                    locationsListView.getItems().add(location))),
                    s -> Platform.runLater(() -> errorLabel.setText(s)));
        }
        if (!searchTextField.getText().isEmpty()) {
            searchTextField.setText("");
        }
    }

    @FXML
    public void onLocationSelection() {
        errorLabel.setText("");
        weatherNameLabel.setText("");
        weatherImageView.setImage(null);
        weatherTemperatureLabel.setText("");
        weatherHumidityLabel.setText("");
        weatherWindSpeedLabel.setText("");
        placesListView.getItems().clear();
        Location selectedLocation = locationsListView.getSelectionModel().getSelectedItem();
        if (selectedLocation != null) {
            Coordinates selectedLocationCoordinates = selectedLocation.getCoordinates();
            weatherProvider.getWeatherByCoordinates(selectedLocationCoordinates.getLatitude(),
                    selectedLocationCoordinates.getLongitude(),
                    weather -> Platform.runLater(() -> {
                        weatherNameLabel.setText(weather.getName());
                        weatherImageView.setImage(new Image(weather.getIconUrl()));
                        weatherTemperatureLabel.setText(weather.getTemperature() + "°C");
                        weatherHumidityLabel.setText(weather.getHumidity() + "%");
                        weatherWindSpeedLabel.setText(10.37 + "m/s");
                    }),
                    s -> Platform.runLater(() -> errorLabel.setText(s)));
            placesProvider.getPlacesByCoordinatesInRadius(selectedLocationCoordinates.getLatitude(),
                    selectedLocationCoordinates.getLongitude(), Integer.parseInt(radiusLabel.getText()),
                    places -> Platform.runLater(() -> places.forEach(place -> placesListView.getItems().add(place))),
                    s -> Platform.runLater(() -> errorLabel.setText(s)));
        }
    }

    @FXML
    public void onPlaceSelection() {
        errorLabel.setText("");
        placeAdditionalInfoTextArea.setText("");
        Place selectedPlace = placesListView.getSelectionModel().getSelectedItem();
        if (selectedPlace != null) {
            placeAdditionalInfoTextArea.setText("Image URL: " + selectedPlace.getImageUrl() + "\n\n" +
                    "Wikipedia URL: " + selectedPlace.getWikipediaUrl());
        }
    }

}
