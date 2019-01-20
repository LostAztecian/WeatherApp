package ru.stoliarenkoas.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private String cityName;
    private String currentWeather;

    private boolean weatherVisible;

    private boolean showHumidity;
    private boolean showPressure;
    private boolean showTemperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherVisible = getWeatherFragmentVisibility();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getParameters();
        updateWeather();
        if (weatherVisible) showWeather();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void confirmSelection(View view) {
        getParameters();
        updateWeather();

        if (!weatherVisible) {
            Intent intent = new Intent(this, CityWeatherActivity.class);
            intent.putExtra("cityName", cityName);
            intent.putExtra("currentWeather", currentWeather);
            startActivity(intent);
        } else showWeather();

    }

    private void showWeather() {
        ((TextView)findViewById(R.id.main_text_city_name)).setText(cityName);
        ((TextView)findViewById(R.id.main_text_city_weather)).setText(currentWeather);
    }

    private void updateWeather() {
        StringBuilder sb = new StringBuilder("Storming clouds");
        if (showTemperature) sb.append(", 271K");
        if (showHumidity) sb.append(", 31%");
        if (showPressure) sb.append(", 760mm");
        sb.append(".");
        currentWeather = sb.toString();
    }

    private void getParameters() {
        cityName = ((TextView)findViewById(R.id.settings_input_city)).getText().toString();
        if (cityName.isEmpty()) cityName = "Manhattan";

        showHumidity = ((Switch)findViewById(R.id.switch_show_humidity)).isChecked();
        showPressure = ((Switch)findViewById(R.id.switch_show_pressure)).isChecked();
        showTemperature = ((Switch)findViewById(R.id.switch_show_temperature)).isChecked();
    }

    private boolean getWeatherFragmentVisibility() {
        final View currentWeatherFragment = findViewById(R.id.fragment_city_weather);
        return !(currentWeatherFragment == null || currentWeatherFragment.getVisibility() != View.VISIBLE);
    }
}
