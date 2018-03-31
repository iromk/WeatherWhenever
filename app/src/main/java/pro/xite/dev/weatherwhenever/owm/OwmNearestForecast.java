package pro.xite.dev.weatherwhenever.owm;

import java.util.List;

import com.google.gson.annotations.SerializedName;


/**
 * Created by Roman Syrchin on 3/26/18.
 */

public class OwmNearestForecast extends OwmData {

    @SerializedName("list")
    List<ListItem> mainList;

    @SerializedName("city")
    OwmCity city;

    class ListItem {

        @SerializedName("dt")
        String dt;

        @SerializedName("main")
        OwmWeather.Main main;

        @SerializedName("weather")
        List<OwmWeather.WeatherItem> weather;
    }


    public OwmCity getCity() {
        return city;
    }

    @Override
    public String toString() {
        return String.format("Nearest forecast for %s is: \n%s", getCity(), mainList.get(0).weather.get(0).getDescription());
    }
}
