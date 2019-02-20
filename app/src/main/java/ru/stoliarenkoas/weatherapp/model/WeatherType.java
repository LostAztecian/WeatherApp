package ru.stoliarenkoas.weatherapp.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class WeatherType {
    @SerializedName("main") private String type;
    private String description;
    private String icon;
}
