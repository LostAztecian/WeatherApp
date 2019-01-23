package ru.stoliarenkoas.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class CityWeatherActivity extends AppCompatActivity {
    private String cityName = "Manhattan";
    private String currentWeather = "raining 17C";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cityName = extras.getString("cityName");
            currentWeather = extras.getString("currentWeather");
        }
        showWeather();

        if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE){
            onBackPressed();
        }
    }
    private void showWeather() {
        ((TextView)findViewById(R.id.main_text_city_name)).setText(cityName);
        ((TextView)findViewById(R.id.main_text_city_weather)).setText(currentWeather);
    }
}
