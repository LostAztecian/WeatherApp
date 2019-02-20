package ru.stoliarenkoas.weatherapp.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.stoliarenkoas.weatherapp.WeatherCard;
import ru.stoliarenkoas.weatherapp.model.CityWeather;
import ru.stoliarenkoas.weatherapp.model.Coordinates;
import ru.stoliarenkoas.weatherapp.model.WeatherClouds;
import ru.stoliarenkoas.weatherapp.model.WeatherCountry;
import ru.stoliarenkoas.weatherapp.model.WeatherMain;
import ru.stoliarenkoas.weatherapp.model.WeatherType;
import ru.stoliarenkoas.weatherapp.model.WeatherWind;

public class WeatherDataBase implements Closeable {
    private static final String[] allColumns;
    private DataBaseHelper dbHelper;
    private SQLiteDatabase database;
    private Cursor cursor;

    static {
        TableColumns[] values = TableColumns.values();
        allColumns = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            allColumns[i] = values[i].columnName;
        }
    }
    public WeatherDataBase(DataBaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void open() {
        if (database == null || !database.isOpen()) {
            database = dbHelper.getWritableDatabase();
        }
        requestCursor();
        cursor.moveToFirst();
    }

    @Override
    public void close() {
        if (cursor != null) cursor.close();
        if (dbHelper != null) dbHelper.close();
    }

    public void putWeather(@NonNull final CityWeather weather) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.ID.columnName, weather.getId());
        values.put(TableColumns.CITY_NAME.columnName, weather.getCity());
        values.put(TableColumns.COUNTRY_CODE.columnName, weather.getWeatherCountry().getCountry());
        values.put(TableColumns.VISIBILITY.columnName, weather.getVisibility());
        values.put(TableColumns.SUNRISE.columnName, weather.getWeatherCountry().getSunrise());
        values.put(TableColumns.SUNSET.columnName, weather.getWeatherCountry().getSunset());
        values.put(TableColumns.LONGITUDE.columnName, weather.getCoordinates().getLongitude());
        values.put(TableColumns.LATITUDE.columnName, weather.getCoordinates().getLatitude());
        values.put(TableColumns.WEATHER_TYPE.columnName, weather.getWeatherType()[0].getType());
        values.put(TableColumns.WEATHER_DESCRIPTION.columnName, weather.getWeatherType()[0].getDescription());
        values.put(TableColumns.WEATHER_ICON.columnName, weather.getWeatherType()[0].getIcon());
        values.put(TableColumns.TEMP_CURRENT.columnName, weather.getWeatherMain().getTemperature());
        values.put(TableColumns.TEMP_MIN.columnName, weather.getWeatherMain().getMinTemp());
        values.put(TableColumns.TEMP_MAX.columnName, weather.getWeatherMain().getMaxTemp());
        values.put(TableColumns.PRESSURE.columnName, weather.getWeatherMain().getPressure());
        values.put(TableColumns.HUMIDITY.columnName, weather.getWeatherMain().getHumidity());
        values.put(TableColumns.WIND_SPEED.columnName, weather.getWeatherWind().getSpeed());
        values.put(TableColumns.WIND_DEGREE.columnName, weather.getWeatherWind().getDegree());
        values.put(TableColumns.CLOUDS_COVERAGE.columnName, weather.getWeatherClouds().getCoveragePercent());

        database.insertWithOnConflict(DataBaseHelper.TABLE_CITIES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void deleteWeather(@Nullable final CityWeather weather) {
        if (weather == null) return;
        database.delete(DataBaseHelper.TABLE_CITIES, TableColumns.ID.columnName + " = " + weather.getId(), null);
    }

    public List<WeatherCard> getCards() {
        List<WeatherCard> cards = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            cards.add(cursorToWeatherCard());
            cursor.moveToNext();
        }
        cursor.moveToFirst();
        return cards;
    }

    private WeatherCard cursorToWeatherCard() {
        CityWeather weather = new CityWeather();
        weather.setId(cursor.getLong(0));
        weather.setCity(cursor.getString(1));
        weather.setVisibility(cursor.getInt(3));

        WeatherCountry country = new WeatherCountry();
        country.setCountry(cursor.getString(2));
        country.setSunrise(cursor.getLong(4));
        country.setSunset(cursor.getLong(5));
        weather.setWeatherCountry(country);

        Coordinates coordinates = new Coordinates();
        coordinates.setLongitude(cursor.getFloat(6));
        coordinates.setLatitude(cursor.getFloat(7));
        weather.setCoordinates(coordinates);

        WeatherType[] type = new WeatherType[1];
        type[0] = new WeatherType();
        type[0].setType(cursor.getString(8));
        type[0].setDescription(cursor.getString(9));
        type[0].setIcon(cursor.getString(10));
        weather.setWeatherType(type);

        WeatherMain main = new WeatherMain();
        main.setTemperature(cursor.getFloat(11));
        main.setMinTemp(cursor.getFloat(12));
        main.setMaxTemp(cursor.getFloat(13));
        main.setPressure(cursor.getInt(14));
        main.setHumidity(cursor.getShort(15));
        weather.setWeatherMain(main);

        WeatherWind wind = new WeatherWind();
        wind.setSpeed(cursor.getFloat(16));
        wind.setDegree(cursor.getInt(17));
        weather.setWeatherWind(wind);

        WeatherClouds clouds = new WeatherClouds();
        clouds.setCoveragePercent(cursor.getInt(18));
        weather.setWeatherClouds(clouds);

        WeatherCard card = new WeatherCard(weather.getCity());
        card.setBundle(weather);
        card.setCurrentWeather(weather.getWeatherType()[0].getDescription());
        card.setImageResource(String.format("https://openweathermap.org/img/w/%s.png", card.getBundle().getWeatherType()[0].getIcon()));
        return card;
    }

    private void requestCursor() {
        cursor = database.query(DataBaseHelper.TABLE_CITIES, null, null, null, null, null, null);
    }
    private void refreshCursor() {
        int position = cursor.getPosition();
        requestCursor();
        cursor.moveToPosition(position);
    }
}
