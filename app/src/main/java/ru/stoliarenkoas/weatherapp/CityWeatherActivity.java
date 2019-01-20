package ru.stoliarenkoas.weatherapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

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
    }
    private void showWeather() {
        ((TextView)findViewById(R.id.textView_cityName)).setText(cityName);
        ((TextView)findViewById(R.id.textView_cityWeather)).setText(currentWeather);
    }
}
