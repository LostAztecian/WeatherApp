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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
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

    private EditText inputField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        openWeatherService = ((App)getApplication()).getOpenWeatherService();

        cards = new ArrayList<>(1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        prepareCitySelection();
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
                cards.add(new WeatherCard(inputField.getText().toString(), "", R.drawable.cloudy));
                requestWeather(cards.get(cards.size()-1));
            }
        });
    }

    private void createRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        initRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        addFewCards();
    }

    private void addFewCards() {
        cards.add(new WeatherCard("Kyiv", "", R.drawable.cloudy));
        cards.add(new WeatherCard("Moscow", "", R.drawable.cloudy));
        cards.add(new WeatherCard("Washington", "", R.drawable.cloudy));
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
                requestWeather(cards.get(position));
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
                    final String description = response.body().getWeatherType()[0].getDescription();
                    final float temperature = response.body().getWeatherMain().getTemperature() - 273;
                    card.setCurrentWeather(String.format(Locale.ENGLISH, "%s: %.2f°C", description, temperature));
                    adapter.notifyItemChanged(cards.indexOf(card));
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call != null) call.cancel();
    }
}
