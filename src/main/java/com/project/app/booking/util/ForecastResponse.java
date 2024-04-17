package com.project.app.booking.util;


import lombok.Data;

import java.util.List;

@Data
public class ForecastResponse {

    @Data
    public static class WeatherEntry {
        private long dt;
        private WeatherData.Main main;
        private List<WeatherData.Weather> weather;
        private WeatherData.Clouds clouds;
        private WeatherData.Wind wind;
        private int visibility;
        private double pop;
        private WeatherData.Rain rain;
        private WeatherData.Sys sys;
        private String dt_txt;


    }



    private String cod;
    private int message;
    private int cnt;
    private List<WeatherEntry> list;

}
