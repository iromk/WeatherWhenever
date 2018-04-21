package pro.xite.dev.weatherwhenever.data.owm;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import pro.xite.dev.weatherwhenever.data.Weather;
import pro.xite.dev.weatherwhenever.data.Whenever;


/**
 * Created by Roman Syrchin on 3/26/18.
 */

public class OwmNearestForecast extends OwmData implements Whenever {

    @SerializedName("list")
    List<OwmWeather> mainList;

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
        final long asked = date.getTime();
        long lastDelta = Long.MAX_VALUE;

        for(int i = 0; i < mainList.size(); i++) {
            Weather weather = mainList.get(i);
            long found = weather.getDate().getTime();
            long delta = Math.abs(asked - found);
            if(delta < lastDelta) {
                lastDelta = delta;
            } else {
                return weather;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("Nearest forecast for %s is: \n%s", "getCity()", mainList.get(0).getTemperature());
    }
}
