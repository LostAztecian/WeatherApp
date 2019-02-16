package ru.stoliarenkoas.weatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.stoliarenkoas.weatherapp.model.CityWeather;

public class MainActivity extends AppCompatActivity {
    private static final String API_KEY = "94839b6ec98f46155c21e102241c4578";
    private OpenWeatherService openWeatherService;
    private static final String TAG = "QWEQWE";
    private List<WeatherCard> cards;
    WeatherCardAdapter adapter;

    private Switch showHumidity;
    private Switch showPressure;
    private Switch showTemperature;

    private RecyclerView recyclerView;

    private Button confirmSelectionButton;

    private String cityName;
    private String currentWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cards = new ArrayList<>();
        cards.add(new WeatherCard("Kyiv", "", R.drawable.cloudy));
        cards.add(new WeatherCard("Moscow", "", R.drawable.cloudy));
        cards.add(new WeatherCard("Washington", "", R.drawable.cloudy));

        initiateRetrofit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        createRecyclerView();
        prepareCitySelection();
        getParameters();
        updateWeather();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSwitchesState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSwitchesState();
        for (WeatherCard card : cards) {
            requestWeather(card);
            try {
                Thread.sleep(1000); //SERVER DELAY
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initiateRetrofit() {
        openWeatherService = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenWeatherService.class);
    }

    private void requestWeather(@NonNull final WeatherCard card) {
        Call<CityWeather> call = openWeatherService.cityWeather(card.getCityName(), API_KEY);
        call.enqueue(new Callback<CityWeather>() {
            @Override
            public void onResponse(Call<CityWeather> call, Response<CityWeather> response) {
                try {
                    card.setCurrentWeather(response.body().getWeatherType()[0].getDescription());
                    adapter.notifyItemChanged(cards.indexOf(card));
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<CityWeather> call, Throwable t) {

            }
        });
    }

    public void saveSwitchesState() {
        SharedPreferences prefs = getSharedPreferences(TAG, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean("showTemperature", showTemperature.isChecked()).apply();
        edit.putBoolean("showHumidity", showHumidity.isChecked()).apply();
        edit.putBoolean("showPressure", showPressure.isChecked()).apply();
        edit.commit();
    }

    public void loadSwitchesState() {
        SharedPreferences prefs = getSharedPreferences(TAG, MODE_PRIVATE);
        showTemperature.setChecked(prefs.getBoolean("showTemperature", false));
        showHumidity.setChecked(prefs.getBoolean("showHumidity", false));
        showPressure.setChecked(prefs.getBoolean("showPressure", false));
        Log.d(TAG, String.format("Switches state: temp-%b, humid-%b, press-%b%n", showTemperature, showHumidity, showPressure));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Settings pressed!", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void prepareCitySelection() {
        confirmSelectionButton = findViewById(R.id.button_confirm_selection);
        confirmSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParameters();
                updateWeather();
                cards.add(new WeatherCard(cityName, currentWeather, R.drawable.cloudy));
                requestWeather(cards.get(cards.size()-1));
            }
        });
    }

    private void createRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
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
            @Override
            public void onShortClick(View view, int position) {
                Log.d(TAG, cards.get(position).getCityName() + ":" + cards.get(position).getCurrentWeather());
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void updateWeather() {
        StringBuilder sb = new StringBuilder("Storming clouds");
        if (showTemperature.isChecked()) sb.append(", 271K");
        if (showHumidity.isChecked()) sb.append(", 31%");
        if (showPressure.isChecked()) sb.append(", 760mm");
        sb.append(".");
        currentWeather = sb.toString();
    }

    private void getParameters() {
        cityName = ((TextView)findViewById(R.id.settings_input_city)).getText().toString();
        if (cityName.isEmpty()) cityName = "Manhattan";
        showTemperature = ((Switch)findViewById(R.id.switch_show_temperature));
        showHumidity = ((Switch)findViewById(R.id.switch_show_humidity));
        showPressure = ((Switch)findViewById(R.id.switch_show_pressure));
    }

}
