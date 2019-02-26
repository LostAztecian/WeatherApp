package ru.stoliarenkoas.weatherapp;

import lombok.Getter;
import lombok.Setter;
import ru.stoliarenkoas.weatherapp.model.CityWeather;

@Getter @Setter
public final class WeatherCard {
    private String cityName;
    private String currentWeather;
    private String imageResource;
    private CityWeather bundle;

    public WeatherCard(String cityName) {
        this.cityName = cityName;
    }
}
