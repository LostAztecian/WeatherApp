package ru.stoliarenkoas.weatherapp.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class WeatherWind {
    private static final String[] DIRECTIONS = {"East", "North-East", "North", "North-West", "West", "South-West", "South", "South-East"};
    private float speed;
    @SerializedName("deg") private int degree;

    public String getDirection() {
        return DIRECTIONS[((degree-22)/45)%8];
    }
}
