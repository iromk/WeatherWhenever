package pro.xite.dev.weatherwhenever.data.owm;

import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import pro.xite.dev.weatherwhenever.data.ForecastInfo;
import pro.xite.dev.weatherwhenever.data.WeatherInfo;


/**
 * Created by Roman Syrchin on 3/26/18.
 */

public class OwmNearestForecast extends OwmData implements ForecastInfo {

    @SerializedName("list")
    List<OwmWeather> mainList;

    @SerializedName("city")
    OwmCity city;

    class ListItem {

        @SerializedName("dt")
        String dt;

        @SerializedName("main")
        OwmWeather main;

        @SerializedName("weather")
        List<OwmWeather.WeatherItem> weather;
    }


    @Override
    public WeatherInfo getNearestForecast() {
        return mainList.get(0);
    }

    @Override
    public WeatherInfo getLatestForecast() {
        return mainList.get(mainList.size()-1);
    }

    public OwmCity getCity() {
        return city;
    }

    @Override
    public WeatherInfo getWeatherOn(Date date) {
        return null;
    }

    @Override
    public String toString() {
        return String.format("Nearest forecast for %s is: \n%s", getCity(), mainList.get(0).getTemperature());
    }
}
