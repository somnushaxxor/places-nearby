module ru.nsu.fit.kolesnik.placesnearby {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires static lombok;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;


    opens ru.nsu.fit.kolesnik.placesnearby to javafx.fxml;
    exports ru.nsu.fit.kolesnik.placesnearby;
    opens ru.nsu.fit.kolesnik.placesnearby.model.location.graphhopper.payload to com.fasterxml.jackson.databind;
    opens ru.nsu.fit.kolesnik.placesnearby.model.weather.openweather.payload to com.fasterxml.jackson.databind;
    opens ru.nsu.fit.kolesnik.placesnearby.model.place.opentripmap.payload to com.fasterxml.jackson.databind;
}