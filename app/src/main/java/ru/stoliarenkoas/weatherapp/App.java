package ru.stoliarenkoas.weatherapp;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Getter
public class App extends Application {
    private OpenWeatherService openWeatherService;
    List<WeatherCard> cards = new ArrayList<>();

    public App() {
        openWeatherService = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenWeatherService.class);
    }
}
