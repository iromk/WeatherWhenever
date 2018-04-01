package pro.xite.dev.weatherwhenever.data;

import java.util.Date;

/**
 * Created by Roman Syrchin on 3/31/18.
 */
public interface WeatherInfo {

    float getTemperature();
    float getMinTemperature();
    float getMaxTemperature();
    Wind getWind();
    int getPressure();
    Date getDate();
}
