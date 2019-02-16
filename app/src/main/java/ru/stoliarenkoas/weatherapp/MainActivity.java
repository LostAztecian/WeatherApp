package ru.stoliarenkoas.weatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
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

public class MainActivity extends AppCompatActivity {
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
        cards.add(new WeatherCard("Mordor", "Storming Clouds", R.id.image_weather));
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
