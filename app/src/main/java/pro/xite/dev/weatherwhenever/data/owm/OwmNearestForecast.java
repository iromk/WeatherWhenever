package pro.xite.dev.weatherwhenever.data.owm;

import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import pro.xite.dev.weatherwhenever.data.Whenever;
import pro.xite.dev.weatherwhenever.data.Weather;


/**
 * Created by Roman Syrchin on 3/26/18.
 */

public class OwmNearestForecast extends OwmData implements Whenever {

    @SerializedName("list")
    List<OwmWeather> mainList;

    class ListItem {

        @SerializedName("dt")
        String dt;

        @SerializedName("main")
        OwmWeather main;

        @SerializedName("weather")
        List<OwmWeather.WeatherItem> weather;
    }


    @Override
    public Weather getNearestForecast() {
        return mainList.get(0);
    }

    @Override
    public Weather getLatestForecast() {
        return mainList.get(mainList.size()-1);
    }


    @Override
    public Weather getWeatherOn(Date date) {
        return null;
    }

    @Override
    public String toString() {
        return String.format("Nearest forecast for %s is: \n%s", "getCity()", mainList.get(0).getTemperature());
    }
}
