package com.project.app.booking.util;

public interface WeatherApi {
    WeatherData getWeather(String city);
    ForecastResponse forecastWeather(String city);
}
