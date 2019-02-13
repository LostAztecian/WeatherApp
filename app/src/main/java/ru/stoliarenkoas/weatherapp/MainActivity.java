package ru.stoliarenkoas.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.stoliarenkoas.weatherapp.browser.BrowserFragment;
import ru.stoliarenkoas.weatherapp.player.PlayerFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "QWEQWE";
    private ArrayList<WeatherCard> cards;
    WeatherCardAdapter adapter;

    private boolean weatherVisible;
    private View weatherFragmentView;
    private View selectionFragmentView;

    private final PlayerFragment playerFragment = new PlayerFragment();
    private final BrowserFragment browserFragment = new BrowserFragment();
    private final CitySelectionFragment citySelectionFragment = new CitySelectionFragment();
    private final CityWeatherFragment cityWeatherFragment = new CityWeatherFragment();

    private String cityName;
    private String currentWeather;

    private Switch showHumidity;
    private Switch showPressure;
    private Switch showTemperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout_main, new CitySelectionFragment()).commit();

//        weatherVisible = getWeatherFragmentVisibility();
//        CityWeatherFragment weatherFragment = (CityWeatherFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_city_weather);
//        cards = weatherFragment.getCards();

//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(manager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        adapter = new WeatherCardAdapter(cards);
//        adapter.setOnItemClickListener(new WeatherCardAdapter.OnItemClickListener() {
//            @Override
//            public void onLongClick(View view, int position) {
//                cards.remove(position);
//                adapter.notifyItemRemoved(position);
//            }
//        });
//        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else super.onBackPressed();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_weather) {
            Log.d(TAG, "Weather fragment");
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main, citySelectionFragment).addToBackStack("TAG").commit();
        } else if (id == R.id.nav_player) {
            Log.d(TAG, "Player fragment");
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main, playerFragment).addToBackStack("TAG").commit();
        } else if (id == R.id.nav_browser) {
            Log.d(TAG, "Browser fragment");
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main, browserFragment).addToBackStack("TAG").commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private boolean getWeatherFragmentVisibility() {
        selectionFragmentView = findViewById(R.id.fragment_city_selection);
        if ("ghosty".equals(((View)selectionFragmentView.getParent()).getTag())) {
            weatherFragmentView = findViewById(R.id.fragment_city_weather);
            weatherFragmentView.setVisibility(View.INVISIBLE);
            return false;
        }
        return true;
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

    public void confirmSelection(View view) {
        getParameters();
        updateWeather();

        if (!weatherVisible) {
            selectionFragmentView.setVisibility(View.INVISIBLE);
            weatherFragmentView.setVisibility(View.VISIBLE);
        }

        cards.add(0, new WeatherCard(cityName, currentWeather, R.drawable.cloudy));
        adapter.notifyItemInserted(0);
    }
}
