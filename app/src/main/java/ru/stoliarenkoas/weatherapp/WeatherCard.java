package ru.stoliarenkoas.weatherapp;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public final class WeatherCard {
    private final String cityName;
    private String currentWeather;
    private int imageId;
}
