package pro.xite.dev.weatherwhenever.data.owm;

/**
 * Created by Roman Syrchin on 3/26/18.
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import pro.xite.dev.weatherwhenever.data.Weather;
import pro.xite.dev.weatherwhenever.data.Wherever;

/**
 * Represents data provided by the OWM service.
 */
public class OwmWeather extends OwmData implements Weather {

    @SerializedName("cod")
    private Integer code;

    @SerializedName("main")
    private Main main;

    @SerializedName("dt")
    private long utcTime;

    public class Main implements Serializable {
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

    @SerializedName("weather")
    private Descriptive[] descriptive;

    public class Descriptive implements Serializable {
        @SerializedName("id")
        Integer id;
        @SerializedName("main")
        String main;
        @SerializedName("description")
        String description;
        @SerializedName("icon")
        String icon;

    }
    public Float getTemp() {
        return main.temp;
    }

    @Override
    public String getDescription() { return descriptive[0].description; }

    @Override
    public String getIconId() {
        return descriptive[0].icon;
    }

    @Override
    public float getTemperature() {
        return main.temp;
    }

    @Override
    public float getMinTemperature() {
        return main.temp_min;
    }

    @Override
    public float getMaxTemperature() {
        return main.temp_max;
    }

    public int getPressure() {
        return main.pressure.intValue();
    }

    @Override
    public Date getDate() {
        return new Date(utcTime * 1000);
    }

    @Override
    public Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(utcTime * 1000);
//        calendar.setTimeZone(getTimezone());
        return calendar;
    }

    @Override
    public Wherever where() {
        return null;
    }

    @Override
    public Weather on(Date date) {
        return null;
    }

    public String getHumidity() {
        return String.valueOf(main.humidity);
    }

    @Override
    public String toString() {
        return String.format("\n  t = %s,\n  pressure = %s\n  humidity = %s%%",
                getTemp(), getPressure(), getHumidity());
    }
}
