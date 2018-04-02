package pro.xite.dev.weatherwhenever.data;

import java.util.Date;

/**
 * Created by Roman Syrchin on 3/31/18.
 */
public interface Weather {

    String DATA_KEY = "WeatherRelatedData";


    float getTemperature();
    float getMinTemperature();
    float getMaxTemperature();
    int getPressure();
    Date getDate();

    Wherever where();
    Weather on(Date date);
    Whenever whenever();

}
