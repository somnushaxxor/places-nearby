package ru.nsu.fit.kolesnik.placesnearby.model.weather.openweather;

import ru.nsu.fit.kolesnik.placesnearby.model.utils.Mapper;
import ru.nsu.fit.kolesnik.placesnearby.model.weather.Weather;
import ru.nsu.fit.kolesnik.placesnearby.model.weather.openweather.payload.OpenWeatherResponse;

public class OpenWeatherMapper implements Mapper<OpenWeatherResponse, Weather> {

    @Override
    public Weather map(OpenWeatherResponse openWeatherResponse) {
        return new Weather(openWeatherResponse.getInfoList().get(0).getWeatherName(),
                getIconUrl(openWeatherResponse.getInfoList().get(0).getIconName()),
                openWeatherResponse.getMain().getTemperature(), openWeatherResponse.getMain().getFeelsLikeTemperature(),
                openWeatherResponse.getMain().getHumidity(), openWeatherResponse.getWind().getSpeed());
    }

    private String getIconUrl(String iconName) {
        return String.format("https://openweathermap.org/img/wn/%s.png", iconName);
    }

}
