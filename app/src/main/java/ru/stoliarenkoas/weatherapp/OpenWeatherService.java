package ru.stoliarenkoas.weatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.stoliarenkoas.weatherapp.model.CityWeather;

public interface OpenWeatherService {
    @GET("data/2.5/weather")
    Call<CityWeather> cityWeather(@Query("q") String cityName, @Query("appid") String appid);
}
