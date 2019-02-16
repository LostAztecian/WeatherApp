package ru.stoliarenkoas.weatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.stoliarenkoas.weatherapp.browser.BrowserFragment;
import ru.stoliarenkoas.weatherapp.player.App;
import ru.stoliarenkoas.weatherapp.player.AudioPlayer;
import ru.stoliarenkoas.weatherapp.player.PlayerFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "QWEQWE";
    private List<WeatherCard> cards;
    WeatherCardAdapter adapter;

    private final PlayerFragment playerFragment = new PlayerFragment();
    private final BrowserFragment browserFragment = new BrowserFragment();
    private final CitySelectionFragment citySelectionFragment = new CitySelectionFragment();
    private final CityWeatherFragment cityWeatherFragment = new CityWeatherFragment();

    private Switch showHumidity;
    private Switch showPressure;
    private Switch showTemperature;

    private RecyclerView recyclerView;

    private Button playButton;
    private Button confirmSelectionButton;

    private String cityName;
    private String currentWeather;

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

        cards = new ArrayList<>();
        cards.add(new WeatherCard("Mordor", "Storming Clouds", R.id.image_weather));

    }

    @Override
    protected void onStart() {
        super.onStart();
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getFragments().contains(cityWeatherFragment)) {
            prepareCitySelection();
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_weather) {
            Log.d(TAG, "Weather fragment");
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main, citySelectionFragment).addToBackStack("TAG").commit();
        } else if (id == R.id.nav_player) {
            Log.d(TAG, "Player fragment");
            preparePlayer();
        } else if (id == R.id.nav_browser) {
            Log.d(TAG, "Browser fragment");
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main, browserFragment).addToBackStack("TAG").commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private void preparePlayer() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout_main, playerFragment).commitNow();

        playButton = findViewById(R.id.button_play);
        final AudioPlayer player = ((App)getApplication()).getPlayer();
        if (player == null) {
            Toast.makeText(this, "Service down!", Toast.LENGTH_SHORT);
            return;
        }

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!player.isPlaying()) {
                    player.play();
                    playButton.setText(R.string.player_pause);
                } else {
                    player.pause();
                    playButton.setText(R.string.player_play);
                }
            }
        });
    }

    private void prepareCitySelection() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout_main, citySelectionFragment).commitNow();

        confirmSelectionButton = findViewById(R.id.button_confirm_selection);
        confirmSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParameters();
                updateWeather();
                prepareCityWeather();
                cards.add(new WeatherCard(cityName, currentWeather, R.drawable.cloudy));
            }
        });
    }

    private void prepareCityWeather() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout_main, cityWeatherFragment).runOnCommit(new Runnable() {
            @Override
            public void run() {
                createRecyclerView();
                adapter.notifyItemInserted(0);
            }
        }).commit();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
