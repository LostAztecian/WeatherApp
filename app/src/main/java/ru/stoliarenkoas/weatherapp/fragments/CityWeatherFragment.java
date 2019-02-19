package ru.stoliarenkoas.weatherapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import lombok.Getter;
import ru.stoliarenkoas.weatherapp.R;
import ru.stoliarenkoas.weatherapp.WeatherCard;

public class CityWeatherFragment extends Fragment {
    @Getter private ArrayList<WeatherCard> cards = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_weather, container, false);
        setRetainInstance(true);
        return view;
    }
}
