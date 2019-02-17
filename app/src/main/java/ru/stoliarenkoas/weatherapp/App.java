package ru.stoliarenkoas.weatherapp;

import android.app.Application;

import lombok.Getter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Getter
public class App extends Application {
    private OpenWeatherService openWeatherService;

    public App() {
        openWeatherService = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenWeatherService.class);
    }
}
