package ru.stoliarenkoas.weatherapp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter
public final class WeatherCard {
    private final String cityName;
    private String currentWeather;
    private int imageId;
}
