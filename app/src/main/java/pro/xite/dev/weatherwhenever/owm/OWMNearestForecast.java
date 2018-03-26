package pro.xite.dev.weatherwhenever.owm;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;


/**
 * Created by Roman Syrchin on 3/26/18.
 */

public class OWMNearestForecast implements Serializable {

    @SerializedName("list")
    List<ListItem> mainList;

    @SerializedName("city")
    OWMCity city;

    class ListItem {

        @SerializedName("dt")
        String dt;

        @SerializedName("main")
        OWMWeather.Main main;
    }

    public OWMCity getCity() {
        return city;
    }
}
