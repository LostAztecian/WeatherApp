package ru.stoliarenkoas.weatherapp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.stoliarenkoas.weatherapp.model.CityWeather;

@RequiredArgsConstructor
@Getter @Setter
public final class WeatherCard {
    private final String cityName;
    private String currentWeather;
    private String imageResource;
    private CityWeather bundle;
}
