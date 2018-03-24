package pro.xite.dev.weatherwhenever;

import java.io.Serializable;

/**
 * Created by Roman Syrchin on 3/24/18.
 */

public class Forecast implements Serializable {

    private String city;
    private String tip;
    private String weather;

    public Forecast(String city, String weather, String tip) {
        this.city = city;
        this.tip = tip;
        this.weather = weather;
    }

    public String getCity() {
        return city;
    }

    public String getTip() {
        return tip;
    }

    public String getWeather() {
        return weather;
    }
}
