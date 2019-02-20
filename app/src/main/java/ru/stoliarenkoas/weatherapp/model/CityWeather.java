package ru.stoliarenkoas.weatherapp.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class CityWeather {
    private long id;
    private int visibility;
    @SerializedName("name") private String city;
    @SerializedName("sys") private WeatherCountry weatherCountry;
    @SerializedName("coord") private Coordinates coordinates;
    @SerializedName("weather") private WeatherType[] weatherType;
    @SerializedName("main") private WeatherMain weatherMain;
    @SerializedName("wind") private WeatherWind weatherWind;
    @SerializedName("clouds") private WeatherClouds weatherClouds;
}
