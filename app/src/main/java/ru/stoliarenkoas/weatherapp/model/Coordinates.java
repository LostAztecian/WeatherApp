package ru.stoliarenkoas.weatherapp.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Coordinates {
    @SerializedName("lon") private float longitude;
    @SerializedName("lat") private float latiitude;
}
