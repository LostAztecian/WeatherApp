package ru.stoliarenkoas.weatherapp.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class WeatherMain {
    @SerializedName("temp") private float temperature;
    @SerializedName("temp_min") private float minTemp;
    @SerializedName("temp_max") private float maxTemp;
    private float pressure;
    private short humidity;
}
