package ru.stoliarenkoas.weatherapp.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class WeatherWind {
    private static final String[] DIRECTIONS = {"East", "North-East", "North", "North-West", "West", "South-West", "South", "South-East"};
    private float speed;
    @SerializedName("deg") private float degree;

    public String getDirection() {
        return DIRECTIONS[(((int)degree-22)/45)%8];
    }
}
