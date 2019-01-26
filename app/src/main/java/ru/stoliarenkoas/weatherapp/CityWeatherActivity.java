package ru.stoliarenkoas.weatherapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class CityWeatherActivity extends AppCompatActivity {
    private ArrayList<WeatherCard> cards = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //TODO extract data
        }

        if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE){
            onBackPressed();
        }
    }
}
