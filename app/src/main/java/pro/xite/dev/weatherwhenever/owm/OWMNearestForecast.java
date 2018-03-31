package pro.xite.dev.weatherwhenever.owm;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;


/**
 * Created by Roman Syrchin on 3/26/18.
 */

public class OWMNearestForecast extends OWMData {

    @SerializedName("list")
    List<ListItem> mainList;

    @SerializedName("city")
    OWMCity city;

    class ListItem {

        @SerializedName("dt")
        String dt;

        @SerializedName("main")
        OWMWeather.Main main;

        @SerializedName("weather")
        List<OWMWeather.WeatherItem> weather;
    }


    public OWMCity getCity() {
        return city;
    }

    @Override
    public String toString() {
        return String.format("Nearest forecast for %s is: \n%s", getCity(), mainList.get(0).weather.get(0).getDescription());
    }
}
