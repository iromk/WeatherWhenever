package pro.xite.dev.weatherwhenever.data;

import java.util.Date;

/**
 * Created by Roman Syrchin on 3/31/18.
 */
public interface Whenever {

    Weather getNearestForecast();
    Weather getLatestForecast();
//    Wherever getCity();
    Weather getWeatherOn(Date date);

}
