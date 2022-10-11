package ru.nsu.fit.kolesnik.placesnearby.model.weather.openweather.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherResponse {

    @JsonProperty("weather")
    private List<OpenWeatherInfo> infoList;
    private OpenWeatherMain main;
    private OpenWeatherWind wind;

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OpenWeatherInfo {

        @JsonProperty("main")
        private String weatherName;
        @JsonProperty("icon")
        private String iconName;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OpenWeatherMain {

        @JsonProperty("temp")
        private double temperature;
        @JsonProperty("feels_like")
        private double feelsLikeTemperature;
        private int humidity;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OpenWeatherWind {

        private double speed;

    }

}
