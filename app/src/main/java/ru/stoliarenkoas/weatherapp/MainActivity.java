package ru.stoliarenkoas.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<WeatherCard> cards;
    WeatherCardAdapter adapter;

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
        if (!weatherVisible) return;

        cards = new ArrayList<>();
        cards.add(new WeatherCard("Manhattan", "Cloudy, 17C.", R.drawable.cloudy));
        cards.add(new WeatherCard("Mordor", "Storming clouds, 271K.", R.drawable.cloudy));

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new WeatherCardAdapter(cards);
        adapter.setOnItemClickListener(new WeatherCardAdapter.OnItemClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                cards.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getParameters();
        updateWeather();
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
            intent.putExtra("cards", cards); //TODO export data
            startActivity(intent);
        } else {
            cards.add(0, new WeatherCard(cityName, currentWeather, R.drawable.cloudy));
            adapter.notifyItemInserted(0);
        };

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
