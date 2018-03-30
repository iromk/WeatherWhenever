package pro.xite.dev.weatherwhenever.owm;

/**
 * Created by Roman Syrchin on 3/26/18.
 */

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

/**
 * Represents data provided by the OWM service.
 */
public class OWMWeather extends OWMCity implements Serializable {

//    @SerializedName("id")
//    private Integer id;

    @SerializedName("cod")
    private Integer code;

//    @SerializedName("name")
//    private String name;

    @SerializedName("main")
    private Main main;


    public class Main {
        @SerializedName("temp")
        Float temp;
        @SerializedName("pressure")
        Float pressure;
        @SerializedName("humidity")
        Integer humidity;
        @SerializedName("temp_min")
        Float temp_min;
        @SerializedName("temp_max")
        Float temp_max;
    }

    public class WeatherItem {
        @SerializedName("id")
        Integer id;
        @SerializedName("main")
        String main;
        @SerializedName("description")
        String description;
        @SerializedName("icon")
        String icon;

        public String getDescription() { return description; }
    }
    public Float getTemp() {
        return Float.valueOf(main.temp);
    }

    public String getPressure() {
        return String.valueOf(main.pressure);
    }

    public String getHumidity() {
        return String.valueOf(main.humidity);
    }

    public String getCity() { return name; }

    @Override
    public String toString() {
        return String.format("Weather for %s is:\n  t = %s,\n  pressure = %s\n  humidity = %s%%",
                super.toString(), getTemp(), getPressure(), getHumidity());
    }
}
