package ru.stoliarenkoas.weatherapp.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class WeatherMain {
    @SerializedName("temp") private float temperature;
    private int pressure;
    private byte humidity;
    @SerializedName("temp_min") private float minTemp;
    @SerializedName("temp_max") private float maxTemp;
}
