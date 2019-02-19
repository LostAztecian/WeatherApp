package ru.stoliarenkoas.weatherapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.stoliarenkoas.weatherapp.model.CityWeather;

public class MainActivity extends AppCompatActivity {
    private static final String API_KEY = "94839b6ec98f46155c21e102241c4578";
    private static final String TAG = "QWE";
    private List<WeatherCard> cards;
    private WeatherCardAdapter adapter;
    private OpenWeatherService openWeatherService;
    private Call<CityWeather> call;

    private View citySelectionView;
    private View cityWeatherView;
    private EditText inputField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        openWeatherService = ((App)getApplication()).getOpenWeatherService();

        cards = ((App) getApplication()).cards;
    }

    @Override
    protected void onStart() {
        super.onStart();
        prepareCitySelection();
        if (findViewById(R.id.frame_layout_main) != null) {
            citySelectionView = findViewById(R.id.fragment_city_selection);
            cityWeatherView = findViewById(R.id.fragment_city_weather);
            cityWeatherView.setVisibility(View.INVISIBLE);
        } else requestWeather(cards.get(0));
    }

    @Override
    public void onBackPressed() {
        if (citySelectionView.getVisibility() == View.INVISIBLE) {
            citySelectionView.setVisibility(View.VISIBLE);
            cityWeatherView.setVisibility(View.INVISIBLE);
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

    private void prepareCitySelection() {
        inputField = findViewById(R.id.edit_text_input_city);
        createRecyclerView();
        Button confirmSelectionButton = findViewById(R.id.button_confirm_input);
        confirmSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCard(inputField.getText().toString());
                requestWeather(cards.get(0));
            }
        });
    }

    private void addCard(@NonNull final String cityName) {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getCityName().equalsIgnoreCase(cityName)) {
                final WeatherCard card = cards.remove(i);
                cards.add(0, card);
                adapter.notifyItemMoved(i, 0);
                return;
            }
        }
        cards.add(0, new WeatherCard(cityName));
        adapter.notifyItemInserted(0);
    }

    private void createRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        initRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        addFewCards();
    }

    //stub
    private void addFewCards() {
        addCard("Kyiv");
        addCard("Moscow");
        addCard("Warsaw");
        adapter.notifyDataSetChanged();
    }

    private void initRecyclerViewAdapter() {
        adapter = new WeatherCardAdapter(cards);
        adapter.setOnItemClickListener(new WeatherCardAdapter.OnItemClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                cards.remove(position);
                adapter.notifyItemRemoved(position);
            }
            @Override
            public void onShortClick(View view, int position) {
                addCard(cards.get(position).getCityName());
                requestWeather(cards.get(0));
                Log.d(TAG, cards.get(position).getCityName() + ":" + cards.get(position).getCurrentWeather());
            }
        });
    }

    private void requestWeather(@NonNull final WeatherCard card) {
        call = openWeatherService.cityWeather(card.getCityName(), API_KEY);
        call.enqueue(new Callback<CityWeather>() {
            @Override
            public void onResponse(Call<CityWeather> call, Response<CityWeather> response) {
                try {
                    card.setBundle(response.body());
                    final String description = card.getBundle().getWeatherType()[0].getDescription();
                    final float temperature = card.getBundle().getWeatherMain().getTemperature() - 273;
                    final String imageResource = String.format("https://openweathermap.org/img/w/%s.png", card.getBundle().getWeatherType()[0].getIcon());
                    card.setCurrentWeather(String.format(Locale.ENGLISH, "%s: %.2f°C", description, temperature));
                    card.setImageResource(imageResource);
                    adapter.notifyItemChanged(cards.indexOf(card));
                    prepareCityWeather(card);
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CityWeather> call, Throwable t) {
                try {
                    card.setCurrentWeather("old weather: date");
                    adapter.notifyItemChanged(cards.indexOf(card));
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }
        });
    }

    public void prepareCityWeather(@NonNull final WeatherCard card) {
        if (cityWeatherView != null) {
            citySelectionView.setVisibility(View.INVISIBLE);
            cityWeatherView.setVisibility(View.VISIBLE);
        }
        ViewGroup viewGroup = findViewById(R.id.fragment_city_weather);
        Glide.with(viewGroup)
                .load(card.getImageResource())
                .into((ImageView) viewGroup.findViewById(R.id.weather_fragment_image_weather_type));
        ((TextView)viewGroup.findViewById(R.id.weather_fragment_text_city_name)).setText(String.format("%s, %s", card.getBundle().getCity(), card.getBundle().getWeatherCountry().getCountry()));
        ((TextView)viewGroup.findViewById(R.id.weather_fragment_text_weather_description)).setText(card.getBundle().getWeatherType()[0].getDescription());
        ((TextView)viewGroup.findViewById(R.id.weather_fragment_text_temperature_value)).setText(String.format("%.2f°C", card.getBundle().getWeatherMain().getTemperature()-273));
        ((TextView)viewGroup.findViewById(R.id.weather_fragment_text_temperature_max_value)).setText(String.format("%.2f°C", card.getBundle().getWeatherMain().getMaxTemp()-273));
        ((TextView)viewGroup.findViewById(R.id.weather_fragment_text_temperature_min_value)).setText(String.format("%.2f°C", card.getBundle().getWeatherMain().getMinTemp()-273));
        ((TextView)viewGroup.findViewById(R.id.weather_fragment_text_humidity_value)).setText(String.format("%d%%", card.getBundle().getWeatherMain().getHumidity()));
        ((TextView)viewGroup.findViewById(R.id.weather_fragment_text_pressure_value)).setText(String.format("%dPa", card.getBundle().getWeatherMain().getPressure()/10));
        ((TextView)viewGroup.findViewById(R.id.weather_fragment_text_wind_speed_value)).setText(String.format("%.2fm/s", card.getBundle().getWeatherWind().getSpeed()));
        ((TextView)viewGroup.findViewById(R.id.weather_fragment_text_wind_direction_value)).setText(String.format("%d°", card.getBundle().getWeatherWind().getDegree()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call != null) call.cancel();
    }
}
