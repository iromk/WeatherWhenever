package pro.xite.dev.weatherwhenever.data.owm;

/**
 * Created by Roman Syrchin on 3/26/18.
 */

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import pro.xite.dev.weatherwhenever.data.WeatherInfo;
import pro.xite.dev.weatherwhenever.data.Wind;

/**
 * Represents data provided by the OWM service.
 */
public class OwmWeather extends OwmData implements WeatherInfo {

//    @SerializedName("id")
//    private Integer id;

    @SerializedName("cod")
    private Integer code;

    @SerializedName("name")
    private String name;

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
        return main.temp;
    }

    @Override
    public float getTemperature() {
        return main.temp;
    }

    @Override
    public float getMinTemperature() {
        return 0;
    }

    @Override
    public float getMaxTemperature() {
        return 0;
    }

    @Override
    public Wind getWind() {
        return null;
    }

    public int getPressure() {
        return main.pressure.intValue();
    }

    @Override
    public Date getDate() {
        return null;
    }

    public String getHumidity() {
        return String.valueOf(main.humidity);
    }

    @Override
    public String toString() {
        return String.format("Weather for %s, %s is:\n  t = %s,\n  pressure = %s\n  humidity = %s%%",
                name, getTemp(), getPressure(), getHumidity());
    }
}
