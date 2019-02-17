package ru.stoliarenkoas.weatherapp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class WeatherCountry {
    private String country;
    private long sunrise;
    private long sunset;
}
