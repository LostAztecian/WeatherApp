package ru.stoliarenkoas.weatherapp.db;

public enum TableColumns {
    ID("id"),
    CITY_NAME("city"),
    COUNTRY_CODE("country"),
    VISIBILITY("visibility"),
    SUNRISE("sunrise"),
    SUNSET("sunset"),
    LONGITUDE("longitude"),
    LATITUDE("latitude"),
    WEATHER_TYPE("type"),
    WEATHER_DESCRIPTION("description"),
    WEATHER_ICON("icon"),
    TEMP_CURRENT("temperature"),
    TEMP_MIN("temperatureMIN"),
    TEMP_MAX("temperatureMAX"),
    PRESSURE("pressure"),
    HUMIDITY("humidity"),
    WIND_SPEED("windSpeed"),
    WIND_DEGREE("windDegree"),
    CLOUDS_COVERAGE("clouds");

    public final String columnName;

    TableColumns(String columnName) {
        this.columnName = columnName;
    }
}
