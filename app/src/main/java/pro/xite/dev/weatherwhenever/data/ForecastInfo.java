package pro.xite.dev.weatherwhenever.data;

import java.util.Date;

/**
 * Created by Roman Syrchin on 3/31/18.
 */
public interface ForecastInfo {

    WeatherInfo getNearestForecast();
    WeatherInfo getLatestForecast();
    CityInfo getCity();
    WeatherInfo getWeatherOn(Date date);

}
